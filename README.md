# Pane demos for Android

Four live, offline, tappable demos contrasting approaches to dual-pane
layouts on Android — all four drive the same **List → Product → Message**
flow through progressively richer navigation substrates.

- **Demo 0 — Unoptimised baseline (`unoptimised/`).** Three sibling
  Activities, zero dual-pane awareness. No `BoxWithConstraints`, no
  `WindowSizeClass`, no Nav3 — just phone-shaped single-pane screens. On a
  tablet or unfolded foldable the layouts stretch across the full width;
  the "wasted space" baseline the other three demos solve. Launched via
  `UnoptimisedListActivity.intent(context)`.
- **Demo 1 — layout-driven (`legacy/`).** `MessageScreen` uses
  `BoxWithConstraints` to fork between a single-pane and a dual-pane
  layout. Three Activities (`ListActivity` → `ProductActivity` →
  `MessageActivity`); the Message Activity owns both a `MessageViewModel`
  **and** a `ProductViewModel`, so the dual-pane view renders both panes
  under one Activity scope. Reproduces the layout pattern you would find
  in a production contact-form screen, stripped of every collaborator
  that isn't load-bearing for the fork. Launched via
  `ListActivity.intent(context)`.
- **Demo 2 — Nav3-driven, fixed weights (`nav3/`, `resizable = false`).**
  The Demo 1 screens routed through Nav3 + `TwoPaneSceneStrategy` from
  the bundled `nav/` package (`Nav3Navigator`, `rememberNavBackStack`,
  `LocalTwoPaneState`, per-entry `ViewModelStore` decorators). The
  strategy owns the shared toolbar end-to-end — title taken from
  `twoPane("Write reply")` metadata on the Message entry. Isolates
  *what Nav3 buys you* by holding the screens constant relative to
  Demo 1 — diffing Demo 2 against Demo 1 shows only the substrate delta.
  Launched via `NavActivity.intent(context, resizable = false)`.
- **Demo 3 — Nav3-driven, resizable divider (`nav3/`, `resizable = true`).**
  Same screens and same `TwoPaneSceneStrategy` as Demo 2, but constructed
  with `resizable = true` — the strategy renders a user-draggable divider
  between panes. Each pane has a **200dp minimum width**
  (`DEFAULT_MIN_PANE_WIDTH`, file-private in `TwoPaneSceneStrategy.kt`);
  drag deltas past that bound accumulate as no-ops. Isolates *what the
  `resizable` toggle adds* — one Boolean on the strategy constructor.
  Launched via `NavActivity.intent(context, resizable = true)` — same
  Activity as Demo 2, different `EXTRA_RESIZABLE` extra.

All four demos work fully offline and share purpose-built ViewModels
(`common/message/MessageViewModel`, `common/product/ProductViewModel`)
with hardcoded Material 3 composables — no JSON schema parser, no
repository, no DI framework — just the layout fork (or its Nav3
replacement), the `StateFlow` state pipeline, and plain Google-style
ViewModels. Stripped down deliberately so the presentation focuses on
the navigation delta, not the DI graph.

### Running the app

```shell
./gradlew :app:installDebug
```

Launches as **Dual-pane demos**. The hub Activity
(`DemoLauncherActivity`) shows four rows; each starts the Activity
chain for that demo. There is no network dependency — the app is
fully offline — so you can toggle airplane mode on and still get the
whole demo.

Target devices for the full story: a phone (~393 dp wide), a Pixel Fold
(inner screen ~673 dp wide, triggers `FOLDABLE` mode), and a Pixel
Tablet (~1280 dp wide, triggers `WIDE` mode). Full emulator setup +
tap paths live in the Part 2 §B runbook below.

---


### 1. Why dual-pane at all

Android serves a very wide device mix: phones from 4.5" upward, tablets,
foldables (Fold/Flip 4-6, Pixel Fold, Honor Magic V), and ChromeOS. A UI
that only reflows text will look padded and empty on any device wider
than ~600 dp, and destination-based navigation (list → detail) becomes
wasteful on foldables where the user has the physical space to see both
at once.

Two constraints shape our choices:

- **Many apps already ship MVP + XML screens.** Dual-pane refactors have
  to land in *one* screen at a time; the framework cannot demand a
  whole-app migration to work.
- **Some pane pairs are one logical screen; others are two.** A contact
  form showing the ad context is one screen — the form has no
  independent URL, no deep-link, no back-button semantics. An overview
  list showing an edit form is two screens — the edit is a real
  destination with its own lifecycle.

Three mechanisms fall out: **Approach A** (layout-driven, one screen
forks itself) and two Nav3-driven approaches — **Approach B** (fixed
weights) and **Approach C** (user-draggable divider) — where two
`NavEntry`s are claimed side-by-side by a scene strategy. Demos 1 / 2 / 3
each demonstrate one approach, all against the same `List → Product →
Message` flow so the diff isolates the substrate change from the screen
change. Demo 0 is the "before" state the three approaches are compared
against.

### 2. Four demos, side-by-side

| Dimension                        | Demo 0: Unoptimised (`unoptimised/`)                                  | Demo 1: layout-driven (`legacy/`)                                        | Demo 2: Nav3, fixed weights (`nav3/`, `resizable = false`)                                                 | Demo 3: Nav3, resizable (`nav3/`, `resizable = true`)                                                               |
|----------------------------------|-----------------------------------------------------------------------|--------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------|
| Back-stack shape                 | OS Activity back stack: List → Product → Message                      | OS Activity back stack: List → Product → Message                         | One Nav3 back stack: `[List, Product, Message]`                                                            | One Nav3 back stack: `[List, Product, Message]` (identical to Demo 2)                                               |
| Dual-pane rendering              | None — all three Activities render single-pane at any width            | `BoxWithConstraints { if (maxWidth >= 600.dp && !Compact) DualPane else SinglePane }` inside `MessageScreen` | `TwoPaneSceneStrategy` claims the scene when the top entry carries `twoPane(title)` metadata and `TwoPaneMode ≠ SINGLE_PANE` | Same strategy, same claim — but renders `ResizableRow` with a drag-aware middle divider instead of fixed weights    |
| ViewModel graph                  | One VM per Activity (`UnoptimisedMessageActivity` resolves `MessageViewModel`; `UnoptimisedProductActivity` resolves `ProductViewModel`) | `MessageActivity` resolves **two** VMs — `MessageViewModel` + `ProductViewModel` — in the same Activity scope so both panes render under one scope | Per-entry `ViewModelStore` via `rememberViewModelStoreNavEntryDecorator`; each entry resolves its own `MessageViewModel` / `ProductViewModel` independently | Same decorator wiring as Demo 2; the divider ratio additionally survives via `rememberSaveable` inside the scene      |
| Weight logic                     | n/a                                                                   | `useEqualWeights = isFoldable \|\| aspectRatio in 0.8..1.25` → 1:1 else 2:3 (`DETAIL_WEIGHT_WIDE=2f`, `FORM_WEIGHT_WIDE=3f`) | `TwoPaneMode.FOLDABLE` → 1:1, `TwoPaneMode.WIDE` → 2:3 (primary:secondary)                                  | Initial ratio = `primaryWeight / (primaryWeight + secondaryWeight)` then user-driven; swap flips entries, not ratio |
| Pane-swap                        | n/a                                                                   | Local `rememberSaveable` boolean + `movableContentOf` keeps scroll/text state across swap | `TwoPaneScene` owns the swap state; shared toolbar's `SwapHoriz` toggles `isPanesSwapped`. **Fixed branch also flips weights on swap** (WIDE 2:3 → 3:2) | Same shared-scene ownership as Demo 2. Ratio is **position-anchored**: a swap flips entry contents but leaves the divider position untouched |
| Toolbar in dual-pane             | n/a                                                                   | Rendered by `MessageScreen`'s own `Scaffold` above both panes            | Rendered by the strategy — title read from the secondary entry's `twoPane(title)` metadata; pane screens read `LocalTwoPaneState` to suppress their own `Scaffold` | Same as Demo 2 — strategy renders one shared toolbar regardless of divider mode                                     |
| Library dependency               | None — plain Compose                                                   | None — plain Compose                                                     | `androidx.navigation3` + the `nav/` substrate in `:app`                                                     | `androidx.navigation3` + the `nav/` substrate in `:app` (identical to Demo 2)                                       |
| Deep-link into secondary         | Not applicable                                                        | Not applicable — Message is an Activity but all callers go through the Product `startActivity` chain | Free — `Message` is a real `NavKey`; a deep-link handler could push `[List, Product, Message]` onto the stack | Same as Demo 2 — free, since the substrate is identical                                                              |
| Independent process-death restore for the secondary | n/a                                                                   | Yes — but both VMs share the same Activity scope, so they restore as a pair | Yes — each `NavEntry` owns its own `ViewModelStore`                                                        | Yes — same as Demo 2; additionally the divider ratio survives via `rememberSaveable` inside the scene               |
| Cost of adoption                 | Zero                                                                  | Zero library cost; one `BoxWithConstraints` fork, one `rememberSaveable` swap state inside the screen | Nav3 setup, `TwoPaneSceneStrategy` wiring, `twoPane(title)` metadata on secondary entries — plus every pane screen must read `LocalTwoPaneState` to suppress its own `Scaffold` | Same cost as Demo 2 **plus** a `BoxWithConstraints` container-width readback and one `rememberSaveable` `Float` in the scene. **Not** production-ready — see §4 for the a11y and test-coverage gap |
| Best when                        | Nothing is a good fit for this — the baseline exists to show the problem the other three solve | Panes are one logical screen (contact form + its ad context) AND you don't foresee the secondary ever becoming a real destination | You have A-style screens today (visually coupled) but expect them to grow independent identities (deep-links, notifications, distinct lifecycles) tomorrow — Nav3 upgrade cost is one Activity + one sealed `NavKey` | You want tablets/foldables to give users direct control over pane widths — a code-editor / preview pattern where the user's task dictates the split, not the app |

### 3. Shared flow and shared code

Every demo tells the same story:

1. **List** — a paginated grid of 300 real-estate listings
   (`DEMO_LIST_ITEMS` in `common/list/ListItems.kt`,
   `DEMO_LIST_TOTAL = 300`, `PAGE_SIZE = 50`, `PREFETCH_DISTANCE = 10`).
   `LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 280.dp))` so
   phones get one column, tablets get three or more.
2. **Product** — one hardcoded apartment (`ProductUiState.createInitialModel()`)
   with a 5-image carousel (`HorizontalPager` on narrow / `LazyRow` on
   wide), title, price, address, posted-date/visits/id row, 12-feature
   grid, and description. Every list tap navigates to the *same*
   product; `ProductViewModel` takes no arguments.
3. **Message** — a 16-field contact form: one message textarea
   (`MessageGroup`), seven Contact fields — salutation, first name,
   last name, street, ZIP, city, phone (`ContactGroup`), and eight
   Information fields — credit report, employment, income, household
   type, adults, children, pets, move-in date (`InformationGroup`).
   Tapping **Send message** is a no-op (`MessageViewModel.onSendClicked() = Unit`);
   nothing is submitted and no snackbar fires.

All four demos consume the same `common/` package:

- `common/list/ListScreen.kt` + `common/list/ListItems.kt` — the grid.
- `common/product/ProductScreen.kt`-adjacent `ProductContent.kt` +
  `ProductUiState.kt` + `ProductViewModel.kt` — the product body.
- `common/message/MessageContent.kt` + `MessageViewModel.kt` +
  `MessageUiState.kt` + `MessageGroup.kt` + `ContactGroup.kt` +
  `InformationGroup.kt` + `DemoOptions.kt` + `DemoSelectorBottomSheet.kt` +
  `SelectorInputField.kt` + `MessageStrings.kt` — the form.

Each demo supplies its own Activities and (in Nav3's case) a navigation
host; everything below the screen boundary is shared.

### 4. Demo 0: Unoptimised — the baseline

Three Activities, no adaptive logic. Teaching point: *this is the
starting line the other three demos improve on.*

- `UnoptimisedListActivity` hosts a plain `LazyColumn` (not grid) over
  `DEMO_LIST_ITEMS`; row tap → `startActivity(UnoptimisedProductActivity.intent)`.
- `UnoptimisedProductActivity` hosts `UnoptimisedProductScreen` + its
  own `UnoptimisedProductContent` — a vertically stacked
  `HorizontalPager` + metadata + "Contact Seller" button inside one
  scroll (visually distinct from `common/product/ProductContent.kt`,
  which the other demos use).
- `UnoptimisedMessageActivity` hosts `UnoptimisedMessageScreen` with a
  hardcoded `isNarrowPane = true` — the form always renders in its
  narrow single-column variant, regardless of actual container width.

No `BoxWithConstraints`, no `WindowSizeClass`, no Nav3. On a Pixel
Tablet or unfolded Pixel Fold, every screen stretches edge-to-edge and
looks padded. That's the point — this is what you have before you cared
about dual-pane.

### 5. Demo 1: layout-driven (`legacy/`) — annotated code walk

#### 5.1 Three Activities, two ViewModels

`legacy/` is a three-Activity chain:

- `legacy/list/ListActivity` → `startActivity(ProductActivity.intent)`.
- `legacy/product/ProductActivity` → resolves a `ProductViewModel`,
  renders `common/product/ProductContent` — same body the Nav3 demos
  use. On "Contact Seller" tap → `startActivity(MessageActivity.intent)`.
- `legacy/message/MessageActivity` → resolves **both** a
  `ProductViewModel` *and* a `MessageViewModel`, passes both states
  into `MessageScreen`:

```kotlin
class MessageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PaneDemoTheme {
                val viewModel: MessageViewModel = viewModel()
                val productViewModel: ProductViewModel = viewModel()
                val viewState by viewModel.uiState.collectAsStateWithLifecycle()
                val productViewState by productViewModel.uiState.collectAsStateWithLifecycle()

                MessageScreen(
                    viewState = viewState,
                    productViewState = productViewState,
                    viewModel = viewModel,
                    onBackPressed = ::finish,
                )
            }
        }
    }
}
```

Two ViewModels, one Activity scope. The dual-pane view shows the
Product details on one side and the Message form on the other — both
living under the same `MessageActivity`, both restoring from process
death together. This is a smaller version of the per-entry
`ViewModelStore` story the Nav3 demos tell, but with Activity
boundaries doing the scoping instead of Nav3 decorators.

#### 5.2 The fork lives inside the composable

`legacy/message/MessageScreen.kt`:

```kotlin
var isPanesSwapped by rememberSaveable { mutableStateOf(false) }
val activity = LocalActivity.current
val windowSizeClass = activity?.let { calculateWindowSizeClass(it) }

BoxWithConstraints(modifier = modifier) {
    val isTwoPaneLayout =
        maxWidth >= TWO_PANE_MIN_WIDTH &&
        windowSizeClass?.heightSizeClass != WindowHeightSizeClass.Compact

    Scaffold(topBar = { TopAppBar(...) }) { innerPadding ->
        if (isTwoPaneLayout) {
            MessageDualPaneContent(viewState, productViewState, viewModel, isPanesSwapped, ...)
        } else {
            SinglePaneForm(viewState, viewModel, ...)
        }
    }
}

private val TWO_PANE_MIN_WIDTH = 600.dp
```

Two things matter here:

- `BoxWithConstraints` **re-reads `maxWidth` on every recomposition**,
  so a fold / unfold / rotate reflows automatically. No config-change
  plumbing.
- The `isPanesSwapped` boolean above the scaffold is `rememberSaveable` —
  the pane order survives process death.

#### 5.3 Weight logic — foldable OR aspect-ratio, not just width

`legacy/message/MessageDualPaneContent.kt` picks weights by combining a
foldable check with an aspect-ratio check:

```kotlin
val isFoldable = rememberIsFoldableInnerScreen()
val aspectRatio = maxWidth / maxHeight
val useEqualWeights = isFoldable || aspectRatio in NEARLY_SQUARE_MIN_RATIO..NEARLY_SQUARE_MAX_RATIO
val detailWeight = if (useEqualWeights) 1f else DETAIL_WEIGHT_WIDE  // 2f
val formWeight   = if (useEqualWeights) 1f else FORM_WEIGHT_WIDE    // 3f

private const val NEARLY_SQUARE_MIN_RATIO = 0.8f
private const val NEARLY_SQUARE_MAX_RATIO = 1.25f
```

Foldables and near-square tablets get 1:1; genuinely wide containers get
form-favoured 2:3. `rememberIsFoldableInnerScreen()` (from `nav/`)
combines `WindowInfoTracker` folding features with the
`android.hardware.sensor.hinge_angle` system feature.

#### 5.4 One `movableContentOf` subtree per pane

Pane-swap uses `movableContentOf` so the detail and form subtrees
*literally move* between `Row` slots without recomposing their
descendants — scroll positions, text-field state, and `remember` values
all survive the swap:

```kotlin
val detailPaneContent = remember {
    movableContentOf { paneModifier: Modifier ->
        MessageDetailsPane(
            viewState = currentProductViewState,
            modifier = paneModifier,
        )
    }
}
val formPaneContent = remember {
    movableContentOf { paneModifier: Modifier ->
        Column(modifier = paneModifier.fillMaxSize()) {
            MessageContent(
                uiState = currentViewState,
                viewModel = currentViewModel,
                modifier = Modifier.weight(1f),
                isSecondPaneOpen = true,
                isNarrowPane = isNarrowPane,
            )
            MessageSendButton(
                onClick = { currentViewModel.onSendClicked() },
                isSecondPaneOpen = true,
                isFormOnLeft = isPanesSwapped,
            )
        }
    }
}
```

`currentViewState`, `currentProductViewState`, and `currentViewModel`
come from `rememberUpdatedState` so the `movableContentOf` lambdas keep
their `remember` identity while still seeing the latest values —
avoiding both recreating the subtree and reading stale captures.
`MessageDetailsPane` itself is a thin wrapper around
`common/product/ProductArea` (the product carousel + metadata +
features list, trimmed of its own `Scaffold`).

### 6. Demo 2: Nav3-driven, fixed weights (`nav3/`, `resizable = false`) — annotated code walk

#### 6.1 Why Demo 2 exists

Demo 1 is the layout-driven baseline. Demo 2 holds the *screen bodies*
constant and swaps the substrate — same `MessageContent` form, same
`ProductScreen` scaffold, both reused from `common/`, but now routed
through Nav3 + `TwoPaneSceneStrategy` from the bundled `nav/` package.

Diffing **Demo 2 vs Demo 1** shows exactly what Nav3 adds — a sealed
`NavKey` (`NavRoute`), an `entryProvider`, a strategy that owns the
scene and the shared toolbar — and, symmetrically, what it *removes*:
the `BoxWithConstraints` fork, the `TWO_PANE_MIN_WIDTH` constant, the
local `rememberSaveable` `isPanesSwapped` boolean, the whole
`MessageDualPaneContent`/`MessageDetailsPane` pair from `legacy/`.

#### 6.2 File layout

The Nav3 side lives entirely in `nav3/`:

- `nav3/NavActivity.kt` — the **sole host** for both Nav3 demos.
  Activity shell + private `ProductNavHost` composable. Reads
  `EXTRA_RESIZABLE` from the launch intent and forwards it into the
  single `TwoPaneSceneStrategy` constructor.
- `nav3/NavRoute.kt` — sealed `@Serializable NavKey` hierarchy:
  `data object List`, `data object Product`, `data object Message`.
  Payload-free; every list tap goes to the same hardcoded `Product`.
- `nav3/list/ListScreen.kt` — a parallel implementation of
  `common/list/ListScreen` (same grid, pagination, scaffold) that
  swaps the lambda callbacks for direct `navigator.navigate(NavRoute.Product)`
  / `navigator.navigateBack()` calls. Not a wrapper: the two files are
  near-identical siblings. The `common/` version is reused by
  `legacy/list/ListActivity`, which plumbs lambdas instead of
  `Nav3Navigator`.
- `nav3/product/ProductScreen.kt` — reads `LocalTwoPaneState.current`
  and suppresses its own `Scaffold + TopAppBar` when non-null.
- `nav3/message/MessageScreen.kt` — reads `LocalTwoPaneState.current`
  and renders either its own `Scaffold` (single-pane) or just the form
  body (dual-pane; the strategy owns the toolbar).

Shared with `legacy/` via `common/`:
`common/list/ListScreen`, `common/product/ProductContent`,
`common/product/ProductViewModel`, `common/product/ProductUiState`,
`common/message/MessageContent`, `common/message/MessageViewModel`,
`common/message/MessageUiState`, `common/message/DemoOptions`.

#### 6.3 `ProductNavHost`, walked line by line

`nav3/NavActivity.kt` defines a private `ProductNavHost` composable
that mirrors the production Nav3 host pattern — about 35 lines:

```kotlin
@Composable
private fun ProductNavHost(
    resizable: Boolean,
    onFinishActivity: () -> Unit,
) {
    val backStack = rememberNavBackStack(NavRoute.List)
    val navigator = remember(backStack) { Nav3Navigator(backStack, onExit = onFinishActivity) }
    val twoPaneMode = rememberTwoPaneMode()

    val strategy = remember {
        TwoPaneSceneStrategy<NavKey>(
            modeProvider = { twoPaneMode },
            resizable = resizable,
        )
    }

    val provider = remember(navigator) {
        entryProvider<NavKey> {
            entry<NavRoute.List>    { ListScreen(navigator) }
            entry<NavRoute.Product> { ProductScreen(navigator) }
            entry<NavRoute.Message>(metadata = TwoPaneSceneStrategy.twoPane("Write reply")) {
                MessageScreen(navigator)
            }
        }
    }

    val entries = rememberDecoratedNavEntries(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = provider,
    )

    NavDisplay(
        entries = entries,
        onBack = { navigator.navigateBack() },
        sceneStrategies = listOf(strategy),
    )
}
```

Line by line:

- `rememberNavBackStack(NavRoute.List)` returns a `SnapshotStateList<NavKey>`
  seeded with the start route. `rememberSaveable` under the hood;
  process death restores the entry list. **The entire navigation state
  of the demo lives here.**
- `Nav3Navigator` is a thin wrapper around a `NavBackStack`:
  `navigate(destination)` **removes any existing entry equal to
  `destination` and then appends it** (dedup — prevents stacking two
  copies of the same `data object` route). `navigateBack()` pops the
  top entry, or calls `onExit` if the stack is empty.
- `rememberTwoPaneMode()` combines window-size class + foldable posture
  into a `TwoPaneMode` (`SINGLE_PANE` / `FOLDABLE` / `WIDE`). Foldable
  detection takes precedence over width because foldables can report a
  width class smaller than `WindowWidthSizeClass.Expanded` while still
  wanting a 1:1 split. Recomposes on config change; the strategy's
  `modeProvider` lambda picks up the new mode on each `calculateScene`.
- `TwoPaneSceneStrategy` takes two constructor params (plus a `minPaneWidth`
  default): `modeProvider` — called on each `calculateScene` so mode
  changes propagate without recreating the strategy — and `resizable`,
  the Boolean that selects fixed-weight (Demo 2) or draggable (Demo 3)
  rendering. **There is no `onBack` constructor param** — the strategy
  reads `onBack` from the `SceneStrategyScope` receiver inside
  `calculateScene` and threads it into the resulting `TwoPaneScene`.
- `entryProvider` registers three entries. The `twoPane("Write reply")`
  metadata goes **only on `Message`** — the metadata is what makes the
  strategy claim. The metadata key's value is the shared-toolbar title;
  presence of the key is the "two-pane this entry" signal.
- `rememberDecoratedNavEntries` wraps every entry with two decorators:
  - `rememberSaveableStateHolderNavEntryDecorator` — each entry gets
    its own `SaveableStateHolder`, so `rememberSaveable` inside an
    entry survives navigation away and back.
  - `rememberViewModelStoreNavEntryDecorator` — each entry gets its
    own `ViewModelStore`, so `viewModel<ProductViewModel>()` and
    `viewModel<MessageViewModel>()` resolve to independent VMs even
    though they share the host Activity's `ViewModelStoreOwner` chain.
- `NavDisplay` calls `strategy.calculateScene(entries)` first — if it
  returns a `TwoPaneScene`, that's the rendered scene. If it returns
  `null` (single-pane mode, or the top entry has no `twoPane()`
  metadata), `NavDisplay` falls back to its default single-entry
  renderer.

#### 6.4 `calculateScene` — one entry point, both divider modes

`nav/TwoPaneSceneStrategy.kt`:

```kotlin
override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
    if (entries.size < 2) return null
    val top = entries.last()
    val title = top.metadata[TwoPaneKey] ?: return null

    val mode = modeProvider()
    if (mode == TwoPaneMode.SINGLE_PANE) return null

    val under = entries[entries.lastIndex - 1]
    return TwoPaneScene(
        key = "two-pane:${under.contentKey}+${top.contentKey}",
        previousEntries = entries.dropLast(2),
        primary = under,
        secondary = top,
        primaryWeight = mode.primaryWeight,
        secondaryWeight = mode.secondaryWeight,
        title = title,
        onBack = onBack,                       // from SceneStrategyScope receiver
        resizable = resizable,
        minPaneWidth = minPaneWidth,
    )
}

object TwoPaneKey : NavMetadataKey<String>

companion object {
    fun twoPane(title: String) = metadata { put(TwoPaneKey, title) }
}
```

Three surface facts worth landing:

- **One metadata key** — `TwoPaneKey : NavMetadataKey<String>` — carries
  the title as its value. Presence of the key *is* the "two-pane this
  entry" signal; the value *is* the shared-toolbar title.
- **Factory**: `TwoPaneSceneStrategy.twoPane(title)` embeds the string
  on the entry at registration time.
- **`onBack` comes from `SceneStrategyScope`.** It's not a strategy
  field — Nav3's scope receiver delivers the host's `onBack` lambda to
  the strategy, which forwards it into the `TwoPaneScene` so the
  shared toolbar's back arrow can invoke it.

#### 6.5 `MessageScreen` — before and after

**Before** (`legacy/message/MessageScreen.kt`, layout-driven): a
`BoxWithConstraints` fork, a `TWO_PANE_MIN_WIDTH = 600.dp` threshold,
a local `rememberSaveable` `isPanesSwapped`, and a `TopAppBar` with a
`SwapHoriz` action in dual-pane.

**After** (`nav3/message/MessageScreen.kt`, Nav3-driven):

```kotlin
val twoPane = LocalTwoPaneState.current

if (twoPane != null) {
    MessageFormWithSendButton(
        uiState = uiState,
        viewModel = viewModel,
        isSecondPaneOpen = true,
        isFormOnLeft = twoPane.isSecondaryOnLeft,
        modifier = Modifier.fillMaxHeight(),
    )
} else {
    Scaffold(topBar = { TopAppBar(title = { Text("Write reply") }, ...) }) { innerPadding ->
        MessageFormWithSendButton(
            uiState = uiState,
            viewModel = viewModel,
            isSecondPaneOpen = false,
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
        )
    }
}
```

(`MessageFormWithSendButton` wraps `common/message/MessageContent` plus
the send button. The `MessageContent` composable is the same one Demo 1
and Demo 0 use.)

**What went**: `rememberWindowSizeClass()`, `rememberSaveable` for
`isPanesSwapped`, `BoxWithConstraints`, the `SwapHoriz` action in the
toolbar, `TWO_PANE_MIN_WIDTH = 600.dp`.

**What came in**: a single `LocalTwoPaneState.current` read. Non-null
means "scene above me is rendering two panes; I'm the secondary and
the scene owns the toolbar." Null means single-pane and the screen
renders its own `Scaffold + TopAppBar`.

There is no `SideEffect` publishing a chrome spec, no `DisposableEffect`
clearing it. The title is pinned in the `Message` entry's
`twoPane("Write reply")` metadata at nav-registration time, and the
strategy reads it directly when it builds the scene. One place writes
the title; one place reads it; the pane screens are oblivious.

#### 6.6 `ProductScreen` — the primary-pane scaffold suppression

`nav3/product/ProductScreen.kt`:

```kotlin
val isTwoPane = LocalTwoPaneState.current != null

if (isTwoPane) {
    ProductArea(item = viewState, modifier = Modifier.fillMaxSize().verticalScroll(…))
} else {
    Scaffold(topBar = { TopAppBar(title = { Text(viewState.title) }, ...) }) { innerPadding ->
        ProductContent(…)
    }
}
```

When the local is present, `ProductScreen` renders only its body —
otherwise the tablet layout would stack two toolbars on top of each
other. In single-pane the local is `null` and `ProductScreen` renders
its full `Scaffold + TopAppBar`.

**Why doesn't `ProductScreen` need to carry a title?** Because the
toolbar title in dual-pane comes from the *secondary* entry's metadata
— the primary pane's title is irrelevant. The invariant is: **title
lives on the secondary route's `twoPane(title)` metadata; both panes
read `LocalTwoPaneState` only to know whether to draw their own
fallback.**

### 7. Demo 3: Nav3-driven, resizable divider (`nav3/`, `resizable = true`) — annotated code walk

#### 7.1 Why Demo 3 exists

Demo 2 collapsed one axis relative to Demo 1: same screens, different
substrate. Demo 3 collapses one more axis relative to Demo 2: *same
screens **and** same substrate*, but the pane-width mechanism changes
from strategy-owned fixed weights to a user-draggable divider.

Diffing **Demo 3 vs Demo 2** is unusually tight — they share the host
Activity, the screen composables, **and** the strategy class — the
difference reduces to one Boolean the launcher passes to
`TwoPaneSceneStrategy`'s constructor.

#### 7.2 The one-Boolean delta

```kotlin
// DemoLauncherActivity
startActivity(NavActivity.intent(this, resizable = true))   // Demo 3
// vs. resizable = false for Demo 2

// NavActivity
val resizable = intent.getBooleanExtra(EXTRA_RESIZABLE, false)
ProductNavHost(resizable = resizable, …)

// ProductNavHost
val strategy = remember {
    TwoPaneSceneStrategy<NavKey>(
        modeProvider = { twoPaneMode },
        resizable = resizable,            // ← the only Demo-2-vs-Demo-3 difference
    )
}
```

Everything else — the entry provider, the `twoPane("Write reply")`
metadata, the decorators, the `NavDisplay`, every screen file — is
byte-identical between Demo 2 and Demo 3.

#### 7.3 The resizable branch inside `TwoPaneSceneStrategy`

`TwoPaneScene.content` branches on `resizable`. The fixed branch is a
plain `Row` with two weight-sized `Box`es (and a swap-aware weight
flip). The resizable branch delegates to a private `ResizableRow`:

```kotlin
val initialRatio = primaryWeight / (primaryWeight + secondaryWeight)
val dividerRatio = rememberSaveable { mutableFloatStateOf(initialRatio) }
```

The divider ratio is a `Float` in `[0, 1]` representing the *left pane's*
fraction of the usable width (total width minus divider hit box). The
initial value derives from the `TwoPaneMode` weights so first render
matches Demo 2 exactly: `0.5` on `FOLDABLE` (1:1), `0.4` on `WIDE`
(2:3).

`rememberSaveable` with **no `inputs` array** is deliberate. The ratio
persists across configuration changes (rotation) *and* across
`TwoPaneMode` changes (fold → unfold → refold), so a user who dragged
to 30/70 on a folded phone stays at 30/70 when they unfold into
`FOLDABLE`.

```kotlin
BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
    val density = LocalDensity.current
    val totalWidthPx = constraints.maxWidth.toFloat()
    val minPaneWidthPx = with(density) { minPaneWidth.toPx() }
    val dividerWidthPx = with(density) { DIVIDER_HIT_AREA_WIDTH.toPx() }
    val usableWidthPx = totalWidthPx - dividerWidthPx
    val minRatio = minPaneWidthPx / usableWidthPx
    val maxRatio = 1f - minRatio
    val canDrag = minRatio < maxRatio

    // Pin to centre when the container is too narrow to honour minPaneWidth on both sides.
    // Writing to snapshot state during composition is a documented Compose antipattern.
    LaunchedEffect(canDrag) {
        if (!canDrag) dividerRatio.floatValue = 0.5f
    }
```

`BoxWithConstraints` is how a Compose scene reads its own container
width — the parent hasn't laid out yet at `remember`-time, so we can't
compute `minRatio` above the `BoxWithConstraints` scope. Everything
ratio-derived (`minRatio`, `maxRatio`, `usableWidthPx`, `canDrag`) has
to live *inside* the constraints block.

`canDrag` is the below-threshold guard: if `minRatio >= maxRatio`, the
container is too narrow to fit `2 × minPaneWidth + dividerWidth`, so
there is no valid position that satisfies both minimums. The strategy
does **not** fall through to single-pane in this case (mid-demo
fall-through surprises the audience). Instead the ratio is pinned to
`0.5` via `LaunchedEffect` — writing to snapshot state during
composition would race recomposition.

```kotlin
    val draggableState = rememberDraggableState { deltaPx ->
        dividerRatio.floatValue =
            (dividerRatio.floatValue + deltaPx / usableWidthPx).coerceIn(minRatio, maxRatio)
    }

    Row(modifier = Modifier.fillMaxSize()) {
        val leftRatio = dividerRatio.floatValue
        Box(Modifier.weight(leftRatio).fillMaxSize()) { leftEntry.Content() }
        Box(
            Modifier
                .width(DIVIDER_HIT_AREA_WIDTH)
                .fillMaxHeight()
                .then(
                    if (canDrag) {
                        Modifier
                            .draggable(orientation = Orientation.Horizontal, state = draggableState)
                            .pointerHoverIcon(PointerIcon.Hand)
                    } else {
                        Modifier
                    },
                ),
        ) {
            VerticalDivider(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.outlineVariant,
            )
        }
        Box(Modifier.weight(1f - leftRatio).fillMaxSize()) { rightEntry.Content() }
    }
}

private val DIVIDER_HIT_AREA_WIDTH: Dp = 24.dp
private val DEFAULT_MIN_PANE_WIDTH: Dp = 200.dp
```

Three top-level `Box`es inside the `Row`: left pane, divider, right
pane. The divider's `Modifier.width(DIVIDER_HIT_AREA_WIDTH)` carves out
a 24dp lane (invisible hit box); the two panes divide the rest by
`leftRatio` and `1f - leftRatio`. The 24dp lane gives users a fat
touch target on phones while the visible `VerticalDivider` (1dp,
`MaterialTheme.colorScheme.outlineVariant`) stays visually restrained
— macOS/Windows split-view convention.

`Modifier.pointerHoverIcon(PointerIcon.Hand)` is the desktop/ChromeOS
cursor affordance — a no-op on phones.

Note the `.then(if (canDrag) ... else Modifier)` guard: when the
container is too narrow (below the `2 × minPaneWidth + dividerWidth`
threshold), the drag and hover modifiers are omitted so the inert
divider doesn't advertise itself as interactive.

#### 7.4 Divider visual spec

- **Hit area**: 24dp wide invisible `Box`
  (`DIVIDER_HIT_AREA_WIDTH`, file-private).
- **Visible line**: `VerticalDivider` (Material 3), 1dp wide, tinted
  `MaterialTheme.colorScheme.outlineVariant`. Centered horizontally
  inside the hit box via `Modifier.align(Alignment.Center)`.
- **Minimum pane width**: 200dp
  (`DEFAULT_MIN_PANE_WIDTH`, file-private, used by `NavActivity`'s
  strategy construction unchanged).
- **Cursor**: `PointerIcon.Hand` on hover — desktop/ChromeOS only.
- **Feedback**: none beyond the visible drag itself. Deliberately
  restrained; matches macOS/Windows split-view convention.

### 8. Trade-off summary — when to reach for which

Four positions, not two. The choice isn't binary "layout-driven or
Nav3-driven"; it's a spectrum with a **pragmatic middle** — Nav3 shape
but simple screens — that Demo 2 makes concrete, and Demo 3 extends by
letting the user own the split. Demo 0 is the before-state; you don't
ship Demo 0 by design, you ship it by neglect.

**Reach for Approach A (layout-driven, Demo 1) when:**

- Pane B has no independent identity. There is no URL you'd want to
  deep-link to that opens *just* pane B; if the user could arrive
  there directly, it would feel broken. Contact form + product
  context is the archetype.
- You want the smallest possible surface area — one composable, no
  library dependency, no metadata contract to teach the rest of the
  team.

**Reach for Approach B (Nav3 + fixed weights, Demo 2) when:**

- You have A-style screens **today** — panes that are visually coupled,
  ViewModels that could fit on one page, no independent deep-link
  requirement — but you expect them to grow into independent
  destinations **tomorrow**.
- You want the substrate cost paid up front, so the migration to
  independent destinations is one Activity boundary + one sealed
  `NavKey` addition instead of a full rewrite.
- You value **shape consistency** across dual-pane surfaces: every
  dual-pane pair in the codebase looks the same — sealed `NavKey`,
  `entryProvider`, `twoPane(title)` metadata on the secondary.
- You want deep-link and notification navigation for free without
  overhauling the screens themselves.

**What Approach B costs you:**

- One extra module dependency (`androidx.navigation3` + the `nav/`
  substrate), plus the `twoPane(title)` metadata contract on every
  secondary entry.
- Every new dual-pane pair needs two things remembered by whoever writes
  the `entryProvider`: the `twoPane(title)` metadata call on the
  secondary entry, and a `LocalTwoPaneState.current` read in both pane
  screens so they suppress their own `Scaffold` in dual-pane. Miss
  either and the tablet layout double-renders a toolbar or shows the
  wrong title.

**Reach for Approach C (Nav3 + resizable divider, Demo 3) when:**

- You need **per-user layout control**. A code-editor / preview pattern,
  where the split fundamentally depends on the task the user is doing
  right now — nobody's 2:3 default is right for everyone.
- You're presenting to a **wide device-mix audience** where "why is my
  split 2:3?" is a real objection.
- The **pane content varies enough** that no fixed ratio would suit all
  pairs. If the same strategy has to serve both a form-heavy and a
  chat-heavy pair, resizable saves you from tuning weights per pair.

**What Approach C costs you:**

- A `rememberSaveable` `Float` inside the scene — cheap in bytes, but
  you own its serialization forever. If you later move the ratio to
  `SavedStateHandle` or `DataStore` for cross-process-death durability,
  you'll be migrating a schema on this field.
- **Accessibility work is deferred and `resizable = true` is *not* yet
  production-ready.** `Modifier.semantics { role = Role.Button;
  stateDescription = ... }` on the divider, keyboard arrow-key handling
  (`onKeyEvent` → ±5% ratio changes), and `Modifier.focusable()` are
  all TODO.
- **Automated tests are deferred.** Add a `TwoPaneSceneStrategyTest`
  covering both branches of `resizable` before the first non-demo
  caller.

**What Approach A (layout-driven) costs you:**

- No way to deep-link into the pane. If product later asks for a
  push-notification that opens directly on the secondary pane, you
  migrate.
- The pane boundary is an Activity boundary (`MessageActivity`
  resolves both VMs) — adding a third pane means re-plumbing the
  Activity, not just adding a `NavKey`.