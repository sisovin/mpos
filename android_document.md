# 📱 Android Project Document: MPOS (co.peanech.mpos)

## 1. Overview

- **Project:** MPOS (Minimal POS) Android client for the MPOS platform
- **Android Package Name:** `co.peanech.mpos` (unique Play Store identifier)
- **Role & Goal:** Implement a native Android app that mirrors the POS functionality (product browsing, cart management, checkout / invoices) while using the existing Node.js + Express backend that proxies product APIs and contains a demo checkout endpoint.
- **Backend:** Node.js + Express (unchanged) — proxies external product APIs (DummyJSON for dev; WooCommerce for prod) and provides a `/api/checkout` demo endpoint.

---

## 2. App Architecture & Patterns

- **Architecture Pattern:** Clean Architecture (Presentation / Domain / Data), MVVM on the presentation layer with ViewModel, StateFlow, and Compose.
- **Modules & Layers:**
  - app (Android application module)
  - feature-products (compose screens for product catalog)
  - feature-cart (cart + checkout UI)
  - core-network (Retrofit / Ktor, serialization)
  - core-db (Room entities / DAOs)
  - core-data (repositories)
  - core-di (Hilt modules)
  - core-common (utils, logger, analytics wrappers)

---

## 3. Languages & Runtimes

- **Primary language:** Kotlin (use modern syntax, Kotlin Standard Library features)
- **Interop:** Java is available for third‑party libraries; prefer Kotlin but annotate public Java interop where needed.
- **Concurrency:** Kotlin Coroutines + Flow for async and reactive streams. Use virtual threads (Project Loom) only where explicitly compatible with Android tooling; coroutines are the default.
- **JDK / Toolchain:** Use Gradle Toolchains with JDK 21 to align with backend and tooling (Gradle 9 support). Target Android API levels per your compatibility requirements. Use `compileOptions` and toolchain to set JDK 21 compatibility for Gradle tasks only.

---

## 4. Jetpack Libraries (AndroidX) & Patterns

Recommended list & rationale:
- **Jetpack Compose (Material 3):** Declarative UI with responsive layouts for phones/tablets. Compose is the primary UI layer.
- **Navigation Component (Hilt + Compose integration):** Type-safe navigation graph for deep linking and safe args.
- **ViewModel + StateFlow:** State holders in Presentation layer, expose unidirectional flows.
- **Room (Coroutines):** Local cache of Product and Order entities; Room + Flow == smooth offline UI updates.
- **WorkManager:** Background tasks for product sync, upload receipts, and deferred retries (with exponential backoff).
- **DataStore (Proto or Preferences):** Small app settings, theme, feature toggles.
- **CameraX + ML Kit (Barcode scanning):** Capture product barcodes / QR codes. Include torch control, autofocus, and fallback manual input.
- **Paging 3 (optional):** For large product catalogs, use Paging 3 with Room & remote mediator if applying incremental loads.

---

## 5. Dependency Injection

- **Hilt** (primary): DI for app module and subcomponents; prefer Hilt for straightforward integration.
- **Koin (optional):** For experimentation or isolated feature modules if you prefer Koin’s DSL for test harnesses. Do not mix Hilt & Koin within the same module; choose one per module.

---

## 6. Networking & API (Client)

- **HTTP client:** Retrofit with Kotlinx Serialization or Moshi; configure timeouts, logging, and interceptors. Alternatively Ktor client if you prefer an all-Kotlin stack.
- **Interceptor:** Add an `ApiErrorInterceptor` to normalize errors, authentication, and log requests in debug builds only.
- **API endpoints:** Mirror server proxies; the app uses the backend endpoints under `/api` (e.g. `/api/products`, `/api/products/:id`, `/api/checkout`).
- **Caching & resilience:** Leverage `Cache-Control` and Room + RemoteMediator for offline-first behavior.

---

## 7. Persistence & Offline

- **Room entities:** ProductEntity, OrderEntity, CartItemEntity, UserEntity (if local caching required).
- **Sync strategy:** Use WorkManager to sync orders and product updates; ensure idempotent operations on the backend.
- **Offline mode:** Keep read-only capabilities with cached products and allow placing orders offline (queued and synced via WorkManager).

---

## 8. Firebase Services

Targeted services and their recommended usage:
- **Authentication (Auth):** Email/password + OAuth providers (Google). Use Firebase Auth to handle user accounts and tokens.
- **Firestore or Realtime Database:** Store products, sales, orders, and settings (use Firestore with offline persistence for simpler queries). Use server timestamps and FieldValue.serverTimestamp() for recorded time on created orders.
- **Cloud Functions:** Create functions for role assignment, order receipts PDF generation, server-side webhook connectors to payment providers and WooCommerce sync jobs.
- **Cloud Storage:** Save logo assets, receipt PDFs, CSV exports, and large files.
- **Crashlytics & Firebase Analytics:** Capture crashes and app usage metrics; integrate into Release build only.
- **Remote Config:** Feature flags and quick toggling of app behavior (e.g. disable payments in case of emergency).

Security & best practices:
- Use Firebase Custom Claims for RBAC to authorize actions server-side.
- On the client, do not embed admin secrets. For server-driven tokens, proxy via backend when needed.

---

## 9. CameraX + MLKit: Barcode & QR

- Implement a dedicated scanning screen: Preview, AutoFocus, Torch, Frame bounding, and visual feedback.
- Validate code against product sources; try to resolve UPC/EAN/QR to product ID via backend service and present product details.

---

## 10. Security

- **Data:** Use TLS/HTTPS for all network calls — our Express server should be HTTPS in production.
- **Secrets:** Use environment variables & CI secret stores; store keystore info in secure CI; store Firebase config carefully.
- **User data:** Follow Play Store guidelines and privacy policy; ensure PII is protected.

---

## 11. Testing Strategy

- **Unit tests:** JUnit5 + MockK for Kotlin, test Domain + ViewModel logic.
- **Flow tests:** Turbine for Suspended Flow testing.
- **UI Tests:** Compose UI Test + Espresso where necessary.
- **Instrumentation tests:** Use Robolectric for JVM-based UI testing where helpful.
- **E2E:** Use Firebase Test Lab + Flank or local Robolectric + physical devices for cross-device verification.

---

## 12. Static Analysis & Linting

- **Detekt** (Kotlin static analysis), **ktlint** (formatting) and **Android Lint**.
- Enable Gradle’s `check` to run detekt & ktlint; configure CI to fail builds on lint violations.

---

## 13. Build & Tooling

- **Gradle 9**: Configure `gradle.properties` for build cache and parallel execution.
- **Version catalogs:** Manage libs via `libs.versions.toml`.
- **Android Gradle Plugin and Kotlin:** Keep stable versions up to date; adopt LLDB and Kotlin plugin compatible with AGP versions.
- **Gradle Toolchains:** Use JDK 21 toolchain via Gradle to unify runtimes.
- **Build types:** `debug`, `release` and `staging` (optional) with proper signing config and build flavor.

---

## 14. Play Store & Release Strategy

- **Package name:** `co.peanech.mpos` — uniquely identifies the app in Play Console.
- **App signing & keys:** Use Google Play App Signing and keep release keystore in CI secrets.
- **Distribution:** Track alpha / beta / production via Play Console tracks; prefer staged rollout for releases.

---

## 15. CI/CD

- **CI:** GitHub Actions with the following steps:
  1) Run static analysis (ktlint/detekt) and unit tests.
  2) Build debug and run Compose UI tests / instrumented tests against Firebase Test Lab.
  3) Trigger code signing and publish artifacts to Play Console (using service account) on `main` releases.
- **CD:** Use an automated pipeline to upload artifacts to Play Console and deploy the Node backend to a provider (Render/Heroku/Cloud Run) using a secure token.

---

## 16. Backend Integration & Endpoints

The Android app communicates to the existing `server/` Node/Express proxy endpoints. Confirm backend is deployed and reachable by the mobile app.

- GET `/api/products` — returns normalized products.
- GET `/api/products/:id` — returns detailed product.
- POST `/api/checkout` — demo endpoint that sums item totals and returns an `ok/total`.

Env sample for backend: `server/.env` (unchanged)

```env
DATA_SOURCE=dummy
DUMMY_API_URL=https://dummyjson.com/products
WOO_BASE_URL=https://your-shop.com
WOO_CONSUMER_KEY=ck_xxx
WOO_CONSUMER_SECRET=cs_xxx
PORT=3000
```

Security note: Keep WOOCredentials and keys only on the server and never in the Android client.

---

## 17. Screen Flows (Five Screens)

Use the five screens (as in the attached UI reference) to cover core flows. Map them to Compose screens below:

1) **Onboarding / Authentication**
   - Login/Signup via Firebase Auth.
   - Optional: Onboarding tour and quick setup (store config, terminal settings).

2) **Products / Catalog**
   - Grid of products (image + title + price); search & filters.
   - Support barcode scanning quick-add (CameraX + ML Kit).

3) **Product Details / Quick Add**
   - Product detail card with description; select attributes or quantity.
   - Add to cart button.

4) **Cart & Checkout**
   - Table-like layout (No. | Product | Qty | Line Total), quantity +/- controls, remove/clear action.
   - Place order: Trigger `POST /api/checkout`, show printable/exportable receipt PDF.
   - Support offline: Queue checkout with WorkManager if offline.

5) **Dashboard / Orders / Receipt**
   - Recent orders, stats, order details, and receipt printing/exporting.

UX tips:
- Use bottom navigation for primary flows (Products / Cart / Orders), or a top-belt with navigation drawer for bigger screens.
- Make cart accessible as a floating FAB on mobile (like the web bottom action) or as a persistent top-right action on larger screens.

---

## 18. Project Structure (Android focused)

```
mpos/
├─ app/
│  ├─ src/main/java/co/peanech/mpos/
│  │  ├─ di/
│  │  ├─ ui/
│  │  │  ├─ onboarding/
│  │  │  ├─ products/
│  │  │  ├─ cart/
│  │  │  ├─ checkout/
│  │  │  └─ dashboard/
│  │  ├─ data/
│  │  │  ├─ remote/
│  │  │  ├─ local/
│  │  │  └─ repository/
│  │  └─ domain/
│  └─ src/main/res/
├─ build.gradle
└─ buildSrc/libs.versions.toml
```
---

## 19. 📱 UI/UX Specifications (Per Screen)

**🎨 Global Design System**

Design tokens, component styles and accessibility guidelines used across the MPOS app.

 - Colors (Material 3 tokens):
   - Primary: #3366FF (Primary brand blue) — used for primary CTAs, highlights, and interactive elements.
   - OnPrimary: #FFFFFF — text/icon color on primary surfaces.
   - Secondary: #F2F4F7 (surface variant) — used for subtle surface contrast and secondary UI areas.
   - Background: #FAFBFF — app background, neutral and soft to reduce eye strain.
   - Surface: #FFFFFF — used for cards, modals and elevated surfaces.
   - SurfaceVariant: #F7F8FB — soft surface variant for grouped content.
   - Border/Muted: #E6E9F0 — used for outline, separators, and consistent stroke values.
   - Error: #D9534F — for destructive actions and validation feedback.
   - Success: #198754 — success states and confirmations.

 - Typography (scale & semantics):
   - Display (Large): 32sp, Weight = 700 — primary screen headings like "Welcome to M-POS".
   - Headline (Medium): 24sp, Weight = 600 — section headings.
   - Body (Regular): 16sp, Weight = 400 — default paragraph copy.
   - Body (Small): 14sp, Weight = 400 — descriptive copy, metadata.
   - Label (Button): 14sp, Weight = 600 — button labels, caps optional depending on the button style.

 - Spacing & Layout Tokens (8pt grid):
   - Small: 8dp
   - Default: 16dp
   - Large: 24dp
   - XLarge: 40dp
   - Corner radii: 8dp (cards), 12dp (hero images), 24dp (large bottom cards)

 - Elevation & Shadows:
   - ElevatedCard: 4dp elevation, subtle shadow color rgba(33,33,33,0.06)
   - HeroCard: 8dp elevation, stronger shadow rgba(33,33,33,0.10)
   - FAB / Surface: 6dp elevation

 - Imagery & Illustrations:
   - Use hero imagery with 16:9 crop with rounded corners and 6--12dp of content inset inside the card.
   - Use the `onboarding-illustration.png` as a primary hero; keep it lightweight and export at multiple densities.

 - Buttons & Actions:
   - Primary Button (filled): Primary color, OnPrimary text; 16sp label, 16dp vertical padding, corner 12dp.
   - Secondary Button (outline): Border color `Border/Muted`, background `Surface`, 16sp label.
   - Text Button: Minimal text action for contextual options.
   - Floating Action Button: Primary color, icon-only or icon+label for tasks that require quick access.

 - Cards & Panels:
   - Cards have `Surface` background, `Border/Muted` 1dp outline and `ElevatedCard` elevation.
   - Large hero cards (like the onboarding image) use `HeroCard` elevation and bigger corner radius.

 - Icons & Imagery:
   - Use Material icons in the filled/rounded style for consistency.
   - Keep icon size at 24dp for standard controls, 32dp for prominent controls.

 - Motion & Interaction:
   - Use short easing curves (200–300ms) for micro-interactions (button presses, FAB reveal).
   - Use 500–700ms fade+translate for hero content entrance and onboarding steps.

 - Accessibility & Localization:
   - Provide contentDescription for all interactive elements and images (use localized strings).
   - Minimum touch target: 48dp x 48dp for all buttons and touch targets (including list items).
   - Contrast: Ensure text meets WCAG 2.1 AA contrast — primary text on `Background` should be >= 4.5:1.
   - Dynamic font support: Ensure text scales with user accessibility settings (sp & ScaleFactor usage).


### 19.1 📱 Screen 1: Onboarding (Welcome)

Purpose: Introduce MPOS to new users, highlight key benefits, provide primary CTA to start using the app, and provide a secondary signup option.

Note: Use `app/src/main/assets/Onboarding.png` as the full-screen mock for spacing and composition, and `app/src/main/assets/onboarding-illustration.png` for the hero image crop and asset implementation.

Layout & Composition

- Top area: Large, high-impact headline left aligned: "Welcome to M-POS" (Display, 32sp, bold), inset 24dp from the left edge on phones.
- Subheading: 16sp body copy directly below the headline, 8dp spacing.
- CTAs row: Primary (Get Started) and Secondary (Sign Up) buttons placed together with 12dp horizontal spacing. Primary button is the prominent filled button; secondary is an outline.
- Feature bullet list: 3 short bullet points below CTAs for quick benefits (e.g., "Easy checkout and returns", "Track cash & stock daily", "Print invoices and QR payments"). Each bullet uses small body text (14sp) with the `Body (Small)` token.
- Hero illustration: Large hero image card beneath the bullets using `onboarding-illustration.png` asset with 16dp margin and 12dp corner radius. Card uses HeroCard shadow.
- Footer/Swipe indicator: Use a small, centered rounded progress indicator (6–8dp height) that implies there are multiple onboarding screens if they're present.

Visual Style

- Use a calm, credit-card-blue primary color for the primary action button. The CTA style follows the global tokens (Primary filled button).
- The hero image is visually prominent and cropped for 16:9 aspect ratio; provide a faint border and shadow to separate it from the background.
- The layout uses a generous left margin and white or surface variant background to emphasize the hero content.

States & Interactions

- Idle: Headline, bullets and CTAs visible; hero image slightly blurred or low-contrast until the animation completes.
- CTA press: Primary button uses quick color-darken animation (200ms), shadow increase for tactile feedback, and ripple effect if not using ripple-less tappable style.
- Hero animation: The hero image fades in and slides up 12dp on first appearance (300–500ms), then subtle parallax with horizontal swipe gesture (if applicable).
- Feature transitions: If you have multiple onboarding steps, use horizontal swipe with spring easing and page dots to show progress.

Motion and Micro-interactions

- Hero entrance: Fade in + translate Y (300–500ms), delay 80–120ms after text entrance.
- CTA entrancing: Buttons fade in from 0% to 100% with translate Y -8dp to 0dp and scale 0.98 to 1.0 (180–240ms) when the screen appears.
- Bullet item reveal: Staggered fade and slide animations (80ms stagger) as the onboarding screen renders.

Accessibility

- Provide `contentDescription` for the hero image using a resource string like `R.string.onboarding_hero_image_desc` so TalkBack can describe the illustration.
- Ensure CTA labels are localized and have accessible labels. Primary button `Get Started` should have `android:accessibilityHeading="true"` on the headline text.
- Make the `Get Started` button the initial accessibility focus on the screen, but only after a short delay to allow the screen to render.
- For color contrast, ensure primary text on the background is high contrast and icons have descriptive text or accessible labels.

Responsive Behavior (Phones & Tablets)

- Phones: Use a single-column layout with content stacked vertically (headline, copy, CTAs, bullets, hero image).
- Landscape & Large screens: Transition to a two-column layout where text/CTAs are on the left column and hero image is on the right column (left column 40–50% width on tablets). Change font sizes minimally (Display 28sp on compact landscape or small displays).

Localization & Copy Guidance

- Keep headline short and action-oriented — provide alternate copy for locales with longer translations.
- Avoid embedding punctuation in images; localize text and CTA labels in string resources.

Analytics

- Track screen_open event `onboarding_open` and CTA taps `onboarding_get_started`, `onboarding_signup`. Add `variant` or `experiment` attributes for A/B testing copy or image changes.

Illustration Implementation Notes & Asset Guidance

- Keep hero assets under `app/src/main/assets` with 1x/2x/3x exports for density buckets and optimized webp formats to reduce size.
- Provide 24dp margin in the UI to avoid text overlap; compress hero images while preserving brand colors.
- Add `loading` placeholder (shimmer) to the hero image to avoid blank UI while image decodes on slower devices.

Examples of copy for the section:

- Headline: "Welcome to M-POS"
- Subcopy: "A modern POS for small and medium retailers. Track sales, manage your stock, and start your day with confidence."
- Primary CTA: "Get Started"
- Secondary CTA: "Sign Up"

Testing & QA

- Validate the onboarding screen with both bright and dark themes to ensure clarity and contrast.
- Run TalkBack and VoiceOver tests for the initial screen and CTAs to confirm accessibility focus and labels.
- Test with scaled fonts (150% and 200%) and verify layout and truncation behavior for translations.

Developer Hints

- Use `Compose` with `AnimatedVisibility` and `rememberScrollState()` for safe vertical scroll on small screens.
- Use Hilt-provided strings and `ViewModel` to hide/show CTA labels if the user is already authenticated.
- Use `Image` from Coil/Accompanist with `placeholder` and `contentScale = ContentScale.Crop` and `clip(RoundedCornerShape(12.dp))`.

### 19.2 📱 Screen 2: Signin (authentication)

Purpose: Authenticate returning users quickly and securely while clearly offering options for password reset and social sign-in.

Note: Reference `app/src/main/assets/Signin.png` for visual composition and `app/src/main/assets/Signin-illustration.png` (if available) for small hero/illustration usage behind the form.

Layout & Composition

- Top area: Small hero illustration or logo centered with 24dp top padding (or left-aligned for two-column layouts). Keep the headline short: "Sign in" (Headline 24sp) with a small explanatory subtitle below (Body 14sp) describing what signing-in unlocks.
- Form fields: Stacked vertically: Email/Phone input, Password field, and a toggle to show/hide password. Each input should have a 16dp vertical gap and use Material filled/outlined `TextField` depending on the theme.
- Actions: Primary CTA `Sign in` (full-width primary filled button), Secondary action in-line with a `Forgot password?` text button aligned right within the form area.
- Social sign-ins: Optional horizontal row of social auth buttons — Google, Apple (iOS), or others — separated by a thin divider (the word `or` centered with lines either side for visual separation).
- Secondary navigation: A small inline link titled "Don't have an account? Sign up" under the fields leading to the Signup screen.

Visual Style

- Inputs use surface background with `Border/Muted` 1dp stroke and 8dp corner radius. The focused field uses a subtle Primary color ring.
- Use inline validation messages under fields in Body (Small, 12-14sp) and error color `Error` for invalid entries.
- Keep background light and forms centered with a maximum width of 560dp on tablets but full width on phones with 16–24dp internal padding.

Validation & Error Handling

- Client-side validation: Show immediate inline errors when the user leaves a required field blank or the email format is invalid.
- Rate-limit or show a generic message on failed sign-ins to avoid revealing account existence (e.g., "Unable to sign in. Confirm your credentials or reset your password.").
- Show a spinner/ripple overlay on the primary CTA once clicked to indicate in-flight requests; disable the button until the request resolves.

States & Interactions

- Idle: Form visible with placeholders in each field; primary CTA enabled only when valid fields are present.
- In-flight: Primary CTA show spinner; fields may be temporarily disabled or still editable depending on UX decisions to support quick retries.
- Error: Inline error under the field or a global toast/snackbar with `Error` color for non-field-specific errors.
- Success: Navigate to the app's home or the previously attempted action; show a brief success animation or subtle toast in the background.

Security Notes

- Use secure fields for the password input and do not store plaintext passwords client side.
- Use HTTPS and secure token handling. If using Firebase, use its recommended flow for sign-in with proper token handling and refresh.
- Avoid storing sensitive data in logs; privilege any error messages that may expose sensitive info.

Accessibility

- Provide accessible label and accessibility hint for the password toggle ("Show password / Hide password").
- Add `contentDescription` for social sign-in icons and buttons (text like "Sign in with Google").
- Ensure `Forgot password?` is a text button with the same focusable target size as regular buttons.

Analytics

- Track `signin_open`, `signin_attempt`, `signin_success`, `signin_fail`, and `forgot_password_tap` events. Include an `auth_method` parameter for social vs classic.

Developer Hints

- Use `TextField` with `keyboardOptions` (email/phone) and `VisualTransformation` (password). Use `remember { mutableStateOf(...) }` to manage input states.
- For security: use `EncryptedSharedPreferences` or token store for persistent auth tokens and keep tokens in the system keystore.
- Add `testTags` to primary CTA and `TextField`s for deterministic UI testing.


### 19.3 📱 Screen 3: Signup (authentication)

Purpose: Allow new users to create an account with a simple, fast signup form and minimal friction, supporting email/password or social sign-ups and optionally capturing the store name or country.

Note: Reference `app/src/main/assets/Signup.png` for composition and hero illustrations.

Layout & Composition

- Top area: Use a small brand logo and headline like "Create account" (24sp) with a concise tagline: "Get set up in minutes" (14sp).
- Form fields: A stacked progression for Email/Phone, Password, Confirm Password, and optional additional fields such as Store Name (optional), and Country/Region (select) if the product requires store-specific defaults.
- Actions: Primary CTA `Create account` styled as full-width primary button. Secondary `Sign in` link below to switch to existing account flow.
- Terms & Opt-ins: Include a subtle checkbox to accept Terms and Privacy Policy and optionally an Email newsletter opt-in with appropriate links. These items must be accessible/labelled and required only where necessary.

Visual Style

- Follow the brand tokens in the Global Design System; display subtle helper text below fields for password strength indicators and format requirements.
- If password strength is weak, provide progress bars or visual feedback for improvement.

Validation & Error Handling

- Validate email format and password security on change. Use inline errors for invalid fields, and the primary CTA remains disabled until required fields are valid and required checkboxes checked.
- Use a progressive disclosure / client-side validation to reduce user friction: start with email & password fields, then show optional fields once those are valid, if desired.

States & Interactions

- Idle: Shows empty form with helpful placeholder text.
- In-flight: Button shows spinner and disables while the request completes.
- Success: On success, navigate to onboarding or the home screen and show a success confirmation. If email verification is required, route to a verification step with step instructions.
- Error: Inline field errors and a top-level snackbar for general errors such as account exists or network errors.

Security & Privacy

- Never log passwords; use secure transport over HTTPS.
- If the backend issues tokens or requires email verification, clearly state the expectations to the user post-signup.

Accessibility

- Make checkboxes and links accessible with `contentDescription` and accessible roles / hints.
- For required fields, offer clear error text and attach it to the field's description for screen readers.

Analytics

- Track `signup_open`, `signup_attempt`, `signup_success`, `signup_fail`, and `signup_opt_in_newsletter` events with the optional `signup_method` param (email, Google, Apple).

Developer Hints

- Use `TextField` with property toggles for `keyboardOptions` and `ImeAction` to move between fields properly.
- Implement password strength validator and optional `Create account` enabling while password and confirmation match.
- Add UI tests with various locales and high font scales to verify content truncation, overflow and label clarity.

### 19.4 📱 Screen 4: Dashboard

Purpose: Provide a concise day-start panel and fast access to common actions (Open Day, New Invoice, Reports) while surfacing the product search and quick navigation to the POS screen.

Note: The main hero layout is illustrated by `app/src/main/assets/Dashboard.png`. Use the asset to match visual spacing and card sizing in Compose implementations.

Layout & Composition

- App header: Left aligned app title (`M-POS`) with a secondary status indicator on the right (e.g., "Day closed" / "Day opened") and a trailing overflow menu or profile avatar.
- Top quick area: Row consisting of a left-side primary action card / button (e.g., Start Cashier Duty / Start Day) and a right-side search input occupying the remaining width that accepts product search queries.
- Open day card: A prominent card titled `Open day` with fields such as `Cash on hand`, `Stock opening value (optional)`, `Cashier name` and a primary `Start Day` secondary CTA `Save draft` if applicable. The card uses `HeroCard` style with `Surface` background and `ElevatedCard` shadow.
- Quick actions card: Row card for quick tasks such as `New Invoice`, `Reports`, `Open POS / Products Dashboard`. Buttons are styled per Global Design System (Primary filled and Secondary outline buttons).
- Products card: A card linking to the POS/Product dashboard with a descriptive header and a prominent CTA to `Open POS / Products Dashboard`.
- Scroll: The dashboard is vertically scrollable on small screens; use `rememberScrollState()` and `Column` with consistent vertical spacing.

Visual Style & Tokens

- App header: Title uses `Display (Large)` or `Headline` typography depending on visual prominence. Status uses Body (Small) and muted color `Border/Muted` for subtlety.
- Buttons: Primary action (Start Cashier Duty / Start Day) uses the brand primary color; `New Invoice` uses the primary color too. Outline buttons use `Border/Muted` and `Surface` background.
- Cards: Use `HeroCard` elevation on the Open day card and `ElevatedCard` on smaller cards. Use consistent 16dp container padding inside cards and 12dp between components.
- Inputs: Use the `TextField` tokens and `Border/Muted` stroke for consistency with forms across the app.

States & Interactions

- Open day status:
  - Day closed (default): The left primary Start Day action is visible and prominent; the `Open day` form is accessible from a card below.
  - Day open: Toggle to `Close Day` and show a small badge with the session summary (cash on hand, sales count) in the header or a compact summary card.
- Search: The search input provides suggestions and typeahead; hitting Enter or using the search icon opens the product search panel or product results screen.
- Quick actions: Each button triggers a navigation event — `New Invoice` opens invoice creation with a new cart; `Reports` opens the reporting screen; `Open POS` opens the product POS dash.
- Card actions: Buttons inside cards are immediate — e.g., pressing `Start Day` validates the form, calls the start-day API and shows an in-card success confirmation.

Open Day Flow

- On pressing `Start Day`:
  - Validate `Cash on hand` is present and numeric. If missing, show inline error (Error color under field).
  - Lock the `Start Day` button and show a spinner during submission.
  - On success: update header status to `Day opened`; show a brief success toast or snackbar with summary (e.g. "Day started — Cash on hand: $0.00").
  - On failure: show snackbar with error and keep the form for retry.

Quick Actions & Keyboard Interactions

- The `Search products` field should use keyboardAction.Search and the IME to provide `Next` or `Search` accordingly. On mobile keyboard `Search` or submit, the app navigates to a full search results screen.
- Buttons should be reachable via keyboard; ensure `contentDescription` for icons and test tabs for physical keyboard navigability.

Responsive Behavior

- Phone portrait: Stack everything vertically and use full-width components with 16dp inset.
- Tablet / Landscape: Use a two-column layout with the left column showing the Open day card and quick actions, and the right column for product search and product highlights.

Accessibility

- Ensure `contentDescription` and `semantics` are present for all interactive elements, and keep touch targets 48dp.
- For the `Open day` form: mark field labels explicitly as accessible labels and use `error` semantics so that validation errors are read by TalkBack.
- Ensure the spinner and loading states have appropriate announcements to screen readers and focus management after completion.

Analytics

- Track `dashboard_open`, `dashboard_start_day_attempt`, `dashboard_start_day_success`, `dashboard_start_day_failed`, and `dashboard_quick_action_{invoice,reports,pos}`.
- Track `dashboard_search` events with query text hashed or tokenized to avoid exposing PII.

Developer Hints

- Compose: Build the dashboard with a root `Scaffold` and `LazyColumn` for content; use `Card` composables for each section and `Button` + `OutlinedButton` for actions.
- State management: Use `ViewModel` with a `MutableStateFlow` for `DayStatus` and `UIState` to drive the content and prevent UI invalid states.
- Validation: Use a small utility to parse and validate numeric input for `Cash on hand` and show a localized error string for invalid input.

Testing & QA

- Verify that the `Open day` flow persists the day status and shows a clear `Day opened` badge on the header.
- Simulate offline start-day attempts and verify queuing or graceful error handling.
- Verify the accessibility flow with TalkBack and keyboard navigation including focus order and announcements.

### 19.5 📱 Screen 5: Products (POS Dashboard — product list for the cashier/point of sale)

Purpose: Display the product catalog in a fast, scannable format that lets cashiers quickly search, browse, and add items to the cart while on duty. This screen is the primary selling surface for the cashier and must prioritize speed, clarity, and minimal taps.

Note: Use `app/src/main/assets/Products.png` to match the visual composition, spacing and list/card proportions when implementing this screen. Use "DATA_SOURCE"  from public API:

#### Check API
Invoke-RestMethod -Uri 'https://mpos-olive.vercel.app/api/products'
#### Check SPA route
Invoke-RestMethod -Uri 'https://mpos-olive.vercel.app/pos'

Layout & Composition

- App header: Left-aligned app title (`M-POS — Products`) with a subtle status label and a `Day closed`/`End Duty` action on the right. Below it, a small `Point of Sale` label and optional `Serving cashier session` subtitle may appear.
- Search & Filters: A prominent search field just below header (placeholder: `Search products`) with filter chips or category dropdowns accessible through an icon or collapsed bar. Use micro-animations to expand filters.
- Product listing: Vertical list of product cards (LazyColumn or LazyVerticalGrid on larger screens). Each card contains:
  - Product image (square / 3:4) with rounded corners and subtle shadow.
  - Title (Headline / 16sp), short description snippet (Body - small / 14sp) truncated to 2 lines.
  - Price on the left (bold body 16sp) and a primary `Add to cart` button on the right.
  - The entire card is a clickable area that opens Product Details (with quantity, attributes, and add-to-cart).
- Cart FAB / Bottom bar: Persistent bottom bar with `Cart (N)` pill and a `Checkout` call-to-action showing `Total` amount. Use the primary color for the cart summary CTA or green for Checkout to highlight completion.

Visual Style & Tokens

- Cards: Use `Surface` with `Border/Muted` stroke and `ElevatedCard` elevation. 12–16dp card padding.
- Typography: Title 16sp/600, description 14sp/400, price 16sp/700.
- Buttons: `Add to cart` uses Primary color with OnPrimary text; `Checkout` uses a green success style or Primary depending on brand.
- Images: Use optimized webp assets and round corners with `RoundedCornerShape(8.dp)` for product images.

Performance & Data Loading

- Use Paging 3 with a RemoteMediator backed by Room for infinite scroll with offline caching and fast loading. Items should animate in using a minimal fade / scale to avoid jank.
- Use lazy image loading (Coil) with placeholders and low-resolution placeholders while images are decoded.
- Batch data & prefetch nearby items using Paging's prefetch configuration for smoother scroll.

Interactions & Behaviors

- Quick add: Clicking `Add to cart` instantly adds 1 unit to the cart and shows a short confirmation (snackbar or animated item fly to cart) while updating the cart bar count.
- Quantity selectors: In Product Details open or quick-add modal, allow the user to adjust quantity before adding to cart.
- Product click: Open product details screen with larger image, description, quantity, attributes, and add-to-cart controls.
- Search suggestions & filters: Typeahead suggestions show top matches; filter chips refine the list (category, price range, popular, in-stock).
- Sort: Provide an overflow dropdown with `Sort by` options e.g., Popularity, Price, Best Selling.

Edge Cases & Error Handling

- Out-of-stock: Disable `Add to cart` and show a disabled state + small `Out of stock` label.
- Network errors: Show a non-intrusive error card with retry action (pull-to-refresh behavior recommended).
- Offline: Allow cart to accept items and keep them in local persistence; show an `Offline` banner and queue checkout actions.

Accessibility

- Product images must have `contentDescription` including product title and price (or `null` if the image is purely decorative and the title is sufficient).
- Ensure `Add to cart` has descriptive labels (e.g., `Add Essence Mascara to cart`) and test screen-reader flow for both card content and CTA.
- Provide accessibility value for price and quantity where relevant and ensure high contrast text for prices and CTA labels.

Analytics & Instrumentation

- Track `product_open` with `product_id`, `add_to_cart` with `product_id` and `qty`, `product_search`, `filter_apply` and `sort_change` events. Include `user_role: cashier` and `session_id` for contextual metrics.
- Track Cart summary changes `cart_items_count`, `cart_total` with every item add/remove for near real-time UI changes and usage metrics.

Developer Hints

- Compose implementation: Use `LazyColumn` for phones and `LazyVerticalGrid` for wider screens. Use `Card` composables for product tiles and `Button` / `OutlinedButton` for actions.
- Paging: Wire Paging 3 with a `PagingSource` that fetches `/api/products` with page and size params. Use `RemoteMediator` to store items in Room for offline UX.
- Image loading: Use Coil with `rememberImagePainter`, `placeholder`, and `crossfade` true. Provide proper caching and memory policies.
- Tests: Add Compose UI tests for `Add to cart`, product opening, and search flows; add unit tests for paging and search debounce logic.

Testing & QA

- Verify that `Add to cart` increments the cart count and updates totals correctly and that the cart bottom bar shows the correct number and total.
- Validate behavior for OOS products: card disabled, accurate error message and prevention from adding.
- Test Search: typeahead suggestions, filter combinations, and ensuring the partial-match returns relevant items.

### 19.6 📱 Screen 6: Cart

Purpose: Let operators/cashiers review cart contents, adjust quantities, remove items, view totals, and proceed to checkout. The cart must be quick to edit during checkout sessions and remain robust in offline scenarios.

Note: Use `app/src/main/assets/Cart.png` to match the visual composition, spacing and bottom action bar for the cart screen.

Layout & Composition

- Header: Title `Your Cart` with a close action on the right to dismiss the cart panel. Show small contextual info (session/cart status) when needed.
- Cart list: Vertical `LazyColumn` showing `CartItemCard` for each item with the following elements:
  - Thumbnail image (left)
  - Title and short variant/attributes (two lines max)
  - Quantity controls: `-` and `+` buttons with a numeric value between them
  - Line total (price x qty) aligned to the right
  - Trash icon to the far-right for quick removal
  - Spare UI: show `Qty` label under the title for clarity (small body text) if space
- Summary area: A separator and lines for `Subtotal`, `Tax`, `Discount` (if applicable), and `Total`. Dimensions and spacing follow tokens: 12dp line spacing, 16dp inner padding.
- Action bar (sticky bottom): A persistent action area with `Total: $X` on the left, `Clear` (red outline) and `Checkout` (green primary) buttons on the right.

Visual Style & Tokens

- `CartItemCard`: `Surface` background, `Border/Muted` 1dp stroke, 8dp corner radius, minimal shadow (`ElevatedCard`).
- Quantity controls: Square 40dp tappable areas for `-` and `+` with 8dp radius; the number between uses bold typography.
- Pricing and totals: `Total` uses bold label at 18sp to show emphasis; subtotal and tax use Body (Regular) 16sp with muted color for less prominence.
- Buttons: `Checkout` uses brand success color (green) with white text; `Clear` uses `Error` red for destructive action.

Interactions & Behaviors

- Increase/Decrease qty:
  - Decreasing: If qty becomes 0 and the user presses `-`, confirm `Remove` or allow auto-remove and show `Undo` snackbar for a short timeframe.
  - Increasing: Validate stock availability on each increase (if `inStock` known), show inline error if it exceeds stock.
  - Debounce rapid taps and queue updates to the backend to avoid request storms.
- Remove:
  - Remove a single item with a trash icon; show an `Undo` snackbar action to restore the item if clicked quickly.
- Clear cart:
  - Confirm destructive action with a small dialog (optional setting to skip confirmation for speed). After clear, show `Empty cart` state with `Empty` illustration and `Continue shopping` CTA.
- Checkout:
  - Validate all items and totals, ensure network request runs with a spinner; on success navigate to Payment or Order Confirmation flow. If offline, queue checkout and show an `Offline` status with `Queue checkout` behavior.

States & Edge Cases

- Empty cart: Show an empty list illustration, friendly copy, and `Continue shopping` CTA.
- Item out-of-stock: Strike through the price and disable `Add` in product detail; in cart show an `Out of stock` note and disable `Checkout` until resolved or allow `Backorder` option.
- Error flows: Show `Failed to update qty` inline message and allow retry; log and show non-sensitive error message for user.

Accessibility

- Buttons: `-` and `+` buttons must have `contentDescription` that includes product name or ID for correct context (e.g., "Decrease quantity for Red Lipstick").
- Total and Checkout: Announce the total when cart opens and after each change; annotate `Checkout` as `aria` action or `role="button"` with `contentDescription`.
- Focus: When the cart opens, set focus to the first interactive control for keyboard users (first `CartItem` `-` or `+` control) and ensure announcements of the number of items present.

Analytics & Instrumentation

- Track `cart_open`, `cart_close`, `cart_item_add`, `cart_item_remove`, `cart_item_update_qty`, `cart_clear`, and `cart_checkout` events. Include `session_id` and `cart_items_count` as properties.

Developer Hints

- Compose layout: Use a `ModalBottomSheet` or `Dialog` to host the cart with a `LazyColumn`. For persistent bottom bar, use `BottomBar` composable with `Row` to hold totals and action buttons.
- State management: Use `ViewModel` + `StateFlow` for the cart. Keep `Cart` aggregated in a `CartRepository` backed by Room to persist across sessions or offline flows.
- Optimistic UI updates: Reflect qty changes immediately (optimistic update) and reconcile responses with the server; show `sync` icons for pending changes.
- Debounce: Debounce the `qty` changes by 200-300ms to reduce item-level API calls.

Testing & QA

- Test changes to qty under poor network; verify optimistic updates and final reconciliation.
- Test `Undo` flows after remove or clear and ensure the cart restored correctly.
- Test the accessibility flow with screen readers and keyboard navigation; validate focus order and announcements after each update.

### 19.7 📱 Screen 7: Checkout

Purpose: Provide a secure and clear final checkout flow where the cashier reviews order details, confirms customer information, selects a payment method (cash, card, or QR), optionally collects customer contact details and addresses, and submits the order for processing. This screen doubles as a quick POS payment terminal for in-person flow.

Note: Use `app/src/main/assets/checkout.png` to match layout, spacing, and card composition for input forms and the order summary panel.

Layout & Composition

- Page header: `Checkout` as the title. If the checkout is an overlay or full-screen, include a back/close action and optionally the cart count summary.
- Primary form card (left/top): A white card with inputs for `Full name`, `Email`, `Phone`, and `Address`. These fields should be auto-filled when the cashier selects a customer or scanned from a loyalty card; keep placeholders and form labels consistent.
- Payment method: Radio group or segmented controls for `Cash`, `Card`, and `QR Code` with an immediate visibility change based on selection (Card shows card input + terminal action, QR shows quick QR selection/scan, Cash shows change calculation field).
- Place order CTA: A large `Place order` or `Pay` button under the form. If using Card/QR, the CTA triggers the payment terminal or QR flow; for Cash it confirms the order and registers cash amounts.
- Order Summary (right/bottom): A dark-surface `Order Summary` card listing items, line totals, and a price breakdown showing `Subtotal`, `Shipping`, `Tax`, `Discounts`, and `Total` in bold.

Payment Methods & Flows

- Cash: cashier enters `Received` amount if a partial payment is requested; show `Change` dynamically. `Place order` confirms the transaction.
- Card: Card entry uses a separate secure workflow (Mobile POS terminal SDK or Webview) to process card tokenization. Show status: `Processing`, `Card declined`, `Success`. Do not store PAN locally.
- QR Code: Generate a QR with payment reference for customer to scan or open a QR scanner for the cashier to capture the customer's payment reference. Update order status on callback.

Validation & UX Details

- Validate required fields and show inline error messages (e.g., `Email` format, `Phone` format). Disable `Place order` until required fields + payment method validated.
- The `Order Summary` must be read-only and recalculated live on quantity changes made earlier; include discount and tax breakdown on request.
- The `Place order` CTA should show a `Processing` spinner when inactive and be disabled until payment flow completes.

States & Feedback

- Idle: All inputs visible, CTA ready (if validation passed).
- Processing: The primary CTA transforms into a spinner + `Processing` and prevents duplicate submissions.
- Success: Show `Order placed` confirmation with an optional `Receipt` preview and `Print` option or `Share`/`Export PDF` action. Optionally navigate to `Order details` or show a toast.
- Failure: Show clear error and reason (e.g., `Card declined`, `Network error`) and allow retry or fallback (use `Cash` as fallback if needed).

Security & Data Handling

- No PAN or secure payment fields should be stored in logs or local storage. Use tokenization and third-party payment SDKs where possible.
- All communications to back-end checkout endpoints must be over HTTPS with proper auth headers.

Receipt & Order Confirmation

- Show `Order Confirmation` view with order ID, transaction ID, timestamp, list of items, and total.
- Provide `Print` (via Print Manager / POS printer SDK) and `Email`/`Share` options for receipt distribution.

Accessibility

- Use descriptive labels for payment method choices and set appropriate content descriptions for inputs and CTAs.
- Announce the total after each field change and after the order is successfully placed.

Analytics & Instrumentation

- Track `checkout_open`, `checkout_place_order_attempt`, `checkout_success`, `checkout_failure`, `checkout_payment_method_selected` and `checkout_receipt_print` events. Include `payment_method`, `order_amount`, `discount_applied` and `session_id` attributes.

Developer Hints & Integration

- Use transparent UI patterns for `Card` payments: delegate to a secure hosted page or tokenization SDK where possible; Android Pay / Google Pay integration is recommended.
- Use `ViewModel` to coordinate `Order`, `Payment` and `Receipt` flows; use `StateFlow` to represent `CheckoutState`.
- Implement retry and fallback policies in the `CheckoutRepository` and ensure idempotent checkout endpoint behavior.

Testing & QA

- Validate `Place order` behavior with successful and failed payments across all methods (cash/card/QR).
- Test receipts, prints, and email distribution for both online and queued (offline) checkout cases.
- Verify accessibility announcements and keyboard navigation for screen readers.

### 19.8 📱 Screen 8: ProcessPayment

Purpose: Host the different payment methods (Cash, Card, QR Code) and manage the in-flight, success, and failure states for each method. Reference `app/src/main/assets/qr_code.png` for the QR card layout and modal presentation.

Layout & Composition

- Page header: `Process payment` title with a compact `order summary` button that toggles the order summary card. Include a close/back action that returns to Checkout or Order Summary.
- Payment method selector: Segmented control or radio group with three items: `Cash`, `Card`, `QR Code`.
- Each method shows a single, focused UI area (card) below the selector:
  - Cash: Numeric keypad + `Received amount` input, dynamic `Change` calculation; large `Complete` CTA.
  - Card: Small card entry summary with `Enter card` action (redirect to a secure card terminal or SDK); shows masked card info after tokenization and a `Pay` action.
  - QR Code: A central QR view area with `Show QR` (merchant-facing) and `Scan QR` (customer-facing) tabs. Use `qr_code.png` as illustration for modal/overlay sizes.
- Secondary actions: `Send receipt` and `Email receipt` and `Add tip` (optional) appear once the payment is successful.

Visual Style & Tokens

- Use the brand `Primary` color for confirm CTAs (green for cash success and checkout); `Error` red for declined or failed states.
- Use `Card` surface for each payment area with 16dp padding and 8dp radius. Use `Display`/`Headline` typography for the payment amount.
- Buttons: Primary (`Proceed / Pay`), Tertiary (`Cancel`), and Muted (`Details`) per the Global Design System.

Interactions & Behaviors

- Cash:
  - When `Cash` selected, show a numeric keypad and `Received` field. Compute `Change` live as `received - total` and display in a success green if greater than zero.
  - `Complete` lock: When tapped, lock the button and call the `checkout` endpoint with `payment_method=cash`. On success, show `Order placed` and a `Print / Send receipt` prompt.
- Card:
  - `Card` uses a hosted SDK or secure UI; enter card details via SDK or show a `Tap/Insert card` flow. Do not store PAN locally; only keep tokenized card identifiers.
  - Handle 3DS or tokenization flows by handing off to the SDK and listening for success/failure callbacks. Show `Processing` overlay during tokenization.
- QR Code:
  - `Show QR`: Generate a short-lived QR payload from the backend and render it with the highest contrast. Once created, show `Copy payload` and `Close` actions. Poll for payment status (or support webhook) to mark success.
  - `Scan QR`: Open a camera overlay to scan a customer QR (if the cashier uses the app to scan). Use the scanner to extract payload and process the payment in the backend.
  - If scanning fails, show fallback: manual `enter code` input and `Retry` option.

States & Feedback

- Idle: The method selector is visible and the chosen method card is ready.
- Processing: A full-screen or in-card spinner appears during network / payment SDK processing; the CTA shows a spinner and is disabled.
- Success: Display `Payment confirmed` with amount, transaction id, card/qr meta and show receipts/print options.
- Failure: Show an inline `Error` message and actions (Retry, Switch payment method, or Cancel). On card decline show `Try again or use a different card`.

Security & Data Handling

- Do not store PANs; use tokenization and the payment provider's secure SDK. Use HTTPS and verify server certificates.
- Mask card numbers in UI or use the provided token string (e.g. `•••• •••• •••• 4242`). Never log or persist sensitive values.

Accessibility

- For segmented controls, include `contentDescription` with the selected `payment_method` and ensure the control is reachable by TalkBack. Announce method changes.
- Ensure `Show QR`/`Scan QR` buttons have `contentDescription` (e.g., `Show QR Code for Payment` or `Scan Customer QR`). Camera overlay must include `aria` labels for focusing.

Analytics & Instrumentation

- Track: `processpayment_open`, `processpayment_method_selected`, `processpayment_attempt`, `processpayment_success`, `processpayment_fail`, and method-specific events like `card_tokenization_attempt`, `qr_show`, `qr_scan`.
- Add `session_id`, `order_id`, `payment_method`, and `payment_amount` as event properties (obfuscate if needed for PII).

Developer Hints

- Use Compose `Dialog` or `ModalBottomSheet` for scan/QR overlays. For `Card`, integrate with the provider (Stripe, Adyen, Paystack) using their Android SDK and tokenization flow.
- Use `Flow` and `ViewModel` to poll for QR payment status in a resource-safe manner (exponential backoff, cancel on view dispose).
- Provide `testTags` for `Show QR`, `Scan QR`, and `Complete` CTAs to support reliable UI tests.

Testing & QA

- Test `QR` generation expiry and polling to ensure the UI transitions correctly when a remote webhook triggers success.
- Test 3DS / card declines with sandbox or mock SDKs and verify fallback flows.
- Test camera permission and scanner overlay behavior in low-light and high-glare conditions.

### 19.9 📱 Screen 9: PlaceOrder

Purpose: Confirm the order details and collect final information (customer details, tip, discount, note), then trigger the payment flow. Use `app/src/main/assets/PlaceOrder.png` as a visual guide for layout and CTA composition.

Layout & Composition

- Header: `Place order` with a back/close button to return to checkout; a secondary `Order summary` control to expand/collapse the full order.
- Form area (main card): Request or confirm `Customer` (select or create), `Phone`, `Email` (optional), `Delivery type` (pickup/delivery), `Tip` (suggested + custom), and `Notes`.
- Order summary: Compact summary with itemized list and price breakdown (subtotal, tax, discount, total). This summary should be visible on the right or below the form depending on layout.
- Payment & CTA: Primary `Place order` button triggers the selected payment workflow (delegates to ProcessPayment view); a secondary `Save draft` or `Hold order` action is presented for deferred processing.

Visual Style & Tokens

- Use primary brand color for the prominent CTA. The `Tip` chips use subtle background color and rounded corners (8dp).
- Keep form fields consistent with design tokens — `TextField` tokens, `Label` typography and `Padding` 12–16dp.

Validation & Business Rules

- Required fields: `Customer name` or `Phone` (depending on store policy). Validate phone formatting and email.
- Tip: Default suggests 5/10/15% or a custom amount; compute totals dynamically and update `Total` live as tip or discount fields change.
- Discounts: Apply code or coupon; show `applied` and updated totals.

Interactions & Flow

- Primary `Place order` behaviour:
  - Validate form and required fields; if invalid, show inline errors.
  - Lock CTA and open the `ProcessPayment` screen (pass order, amount, order id and session information).
  - If payment succeeds, show `Order confirmation` and `Print/Share` options.
  - If payment fails, keep the order details and allow retry or switching to another payment method.
- Save Draft: Persist the order in the `CartRepository` or `Drafts` table for later completion.

States & Edge Cases

- Offline: If offline, store the `PlaceOrder` request in a local queue and show `Queued for Offline processing` state. Allow `Cancel` and `Retry` when online.
- Duplicate order detection: If the backend rejects a duplicate order (same order id / amount), surface the server reason and provide `Retry` or `Contact Support` options.

Accessibility

- Ensure all fields have `label` and `contentDescription`. Make the `Tip` chips keyboard-navigable and support `aria` descriptors for the `Place order` CTA.

Analytics

- Track `placeorder_open`, `placeorder_submitted`, `placeorder_success`, `placeorder_fail`, `placeorder_draft_saved` with `order_id` and `cart_total`.

Developer Hints

- Use `ViewModel` to manage order state and `SavedStateHandle` to persist during process lifecycle runs. Use `OrderRepository` to centralize create/hold/commit logic.
- Build a `PlaceOrder` form component and reuse fields/validators across other screens.
- When launching `ProcessPayment`, pass a full DTO with `orderId`, `amount`, `currency`, and `metadata` to support webhooks.

Testing & QA

- Test `Place order` transitions: valid->processing->success, invalid form error(s), and failure->retry.
- Verify tip calculations and rounding across currencies and locales.

### 19.10 📱 Screen 10: PrintInvoice

Purpose: Provide a POS-friendly receipt/print flow for completing sales; support thermal POS printers (Bluetooth / USB / Network) and Android Print Manager for advanced printers. Ensure receipts are structured for printing readability and POS compatibility.

Layout & Composition

- Provide a `Print receipt` screen containing:
  - Header: Brand name, store address (optional), `Order ID` and date/time.
  - Body: Itemized list with `Qty x Price` and `Line total` aligned to the right, with tax, discounts, subtotal and final `Total` emphasized.
  - Footer: `Thank you` message, return policy, contact or loyalty number, `Transaction ID`, and a QR code for digital receipt or tracking (optional).
- Print preview: Offer a `Preview` pane that displays how the plain text or formatted receipt prints on typical 48–80mm thermal paper. Provide a `Fit` option to check layout.

Print Formatting & POS Compatibility

- Use plain text-friendly layout with fixed-width assumptions for thermal printers (columns: name, qty, line total) and keep lines under 32/42 characters depending on printer width. Offer a `compact` and `detailed` version.
- Support `ESC/POS` formatting snippets and also plain text printing for compatibility with older printers. For advanced printers, offer a `PDF` or `bitmap` print job to preserve layout.
- Where available provide `printer_settings` such as `paper_width`, `font_size`, `logo` (monochrome), and `charset` to allow printer-specific rendering.

Device & Printer options

- Local printers: Support Bluetooth and USB printers using vendor SDKs (Sunmi, Star, Epson, etc.). Detect printers with `BluetoothAdapter` and provide pairing helper.
- Network printers: Support print by IP/TCP sockets; discover printers using `mDNS` or use user-supplied IP/port.
- Android Print Manager: Support `PrintDocumentAdapter` for PDF/bitmap printing to a wider set of printers.

Interactions & Flow

- Print action flow:
  - From the `Order confirmation` or `ProcessPayment` success screen, open the `PrintInvoice` view.
  - Choose `Printer` from a list or `Preview` then `Print`.
  - Show progress and status: `Printing`, `Printed`, `Failed` with `Retry`.
  - After printing success, allow `Reprint` and `Email receipt` options.

Security & Compliance

- Sensitive fields: Mask or omit any card PAN or sensitive tokens from receipts; show last 4 digits only when card used.
- Data retention: Receipts should follow regional data privacy rules; provide an option to hide personal customer details on printed receipts if requested.

Developer Hints

- Use a `PrinterService` abstraction with multiple adapters: `EscPosAdapter`, `PdfPrintAdapter`, `VendorSdkAdapter`. Implement pluggable adapters to allow adding vendor-specific features.
- For `ESC/POS` formatting, build small generator utilities that produce a sequence of bytes and a `ByteArrayOutputStream` to send to printers.
- For PDF and advanced layout, use `Android Print Manager` with a `PrintDocumentAdapter` or create a `Bitmap` and print it as an image.
- Provide `printJob` tracking to surface status (queued, printing, error, completed) and retry policies.

Testing & QA

- Test with at least two types of printers: thermal ESC/POS (Bluetooth) and full drivers (PDF or network driver).
- Verify alignment and column widths across different paper sizes (58mm/80mm), evaluate truncation strategy for long product names.
- Test Reprint and Email receipt flows, including `Retry` and `Printer offline` scenarios.


### 19.11 📱 Screen 11: AfterPrintingDashboard

Purpose: Surface a clear confirmation that an order receipt has been printed successfully and provide quick post-print actions for the cashier (reprint, email, share, new sale, end duty). This screen is shown after `PrintInvoice` finishes or when the cashier explicitly navigates to the summary. See `app/src/main/assets/AfterPrintingDashbard.png` for composition and CTA location.

Layout & Composition

- Top area: Large success icon (green check) with headline `Receipt printed` or `Printed successfully` and smaller `Order ID`, `Amount`, `Transaction ID` and `Time` as a compact meta row.
- Summary card: A short read-only invoice summary card that contains up to 4 highest-level line items (auto-collapsed), subtotal and final total. Include `View full order` to open the order details screen.
- Actions row (primary): `Reprint` (primary), `Email` (secondary), `Share` (outline) — horizontally arranged and sized for touch (minimum 48dp height / 16dp horizontal padding).
- Actions row (secondary): `New sale` (primary) as a green CTA and `End duty` (red/outlined) as a destructive option or `Return to dashboard` link.
- Footer: A subtle link for `Print history` or `Print again` with failed print logs / job info.

Visual Style & Tokens

- Use success color (`Success` or green) for the big check icon and the `New sale` CTA. `Reprint` uses Primary to indicate immediate action; `Email` uses Outlined style.
- Keep typography consistent with the Global Design System: Headline (18–20sp), Subcopy (14sp), and small muted meta text.
- Provide sufficient spacing between actions: 12–16dp vertical spacing and 8–12dp horizontal between CTA buttons.

Interactions & Behaviors

- On open: The screen shows a short success animation for the printed receipt (e.g., check mark fade-in) and an audible subtle chime (optional, controlled by device settings) to confirm success.
- Reprint: When tapped, triggers the `PrintInvoice` flow using the previously selected printer and print settings. Lock the button while printing is in-progress and show a small spinner on the button.
- Email: Shows a small dialog to confirm the email address (prefilled with the last known customer email). Option to save address in customer record (only if property enabled). On success show a toast/snackbar `Receipt emailed to <email>`.
- Share: Opens standard Android `Intent` chooser to send as PDF or text (depending on print job adapter) for non-registered receipts.
- New sale: Clears cart and returns user to `Products` / POS screen (preserve session/day state). Focus should land on the search input or the product list.
- End duty: Confirm destructive Will-close / End-day flow with optional `Print end-of-day` summary.

States & Feedback

- Success: Show `Printed successfully` with transaction id and `Reprint / Email / Share` available.
- Reprinting: Button shows `Printing` with spinner. After success, show `Reprint complete` toast; after failure, show inline error with `Retry` and `Print logs` option.
- Emailing: Show an in-card `Sending` and final `Sent` or `Failed` message; allow retry.
- Offline / Printer disconnected: If printing fails due to printer disconnect, show `Printer Disconnected` with `Retry / Reconnect` and `Queue print job` options. A queued print job should be visible in `Print history`.

Security & Data Handling

- Mask personal or sensitive data in receipts where appropriate; do not include PAN or complete card data — only show `•••• 4242` if permitted.
- Confirm that email actions do not leak PII (use hashed or obfuscated identifiers in analytics or logs) and require user action to store emails persistently.

Accessibility

- Announce success to TalkBack with `Receipt printed for order <order_id> amount <amount>` and ensure the first focusable element is the `Reprint` button after success.
- Ensure `Email` and `Share` dialogs are keyboard accessible and have accessible labels and hints to avoid ambiguity.

Analytics & Instrumentation

- Track `afterprinting_open`, `afterprinting_reprint_attempt`, `afterprinting_reprint_success`, `afterprinting_reprint_fail`, `afterprinting_email_sent`, `afterprinting_email_failed`, `afterprinting_share`, and `afterprinting_new_sale` event types.
- Attach `order_id`, `session_id`, `payment_method` and `printer_id` where applicable. Use hashed or non-PII values for customer identifiers.

Developer Hints & Integration

- Leverage `PrinterService` and `printJob` APIs used by `PrintInvoice` to resume print jobs or re-use the last printer settings. Reprint should use the same `printJob` options unless a user chooses `Change printer`.
- For `Email` and `Share`, use the same generated PDF or plain text format produced by the `PrintInvoice` adapter for parity.
- Implement a small `PrintJobQueue` table in Room to persist failed / offline print jobs that will re-attempt automatically (or manually via `Print history`).
- Provide `testTags` for `Reprint`, `Email`, `Share`, `New sale`, and `End duty` to ease UI tests.

Testing & QA

- Reprint test: Verify reprint uses the same settings and prints again on both USB/Bluetooth and Network printers using a mocked printer adapter.
- Email test: Verify `Email receipt` pre-fills the last email and uses the correct PDF/format; validate send success/failure cases and retry flows.
- Offline test: Simulate disconnected printers and verify job queueing + manual resume in `Print history`.
- Accessibility test: Confirm TalkBack focuses the `Reprint` button and that success messages are announced. Test keyboard navigability and large font fallbacks.

Integration notes

- Consider offering a `Receipt preferences` settings page to control default `Email receipt` and `Auto-print` behavior after every transaction.
- Offer `Auto-archive` for printed receipts if the store needs to keep a copy locally or on server side for X days.

---

## 20. Developer Workflow & Local Setup

- Install Android Studio (Arctic Fox or latest stable), Android SDK, and JDK 21 (Gradle toolchain). Install emulator or use a physical device.
- Set up Firebase project and Android `google-services.json` for Debug and Staging; for production use separate project or environment.
- Configure the Android app to consume the Node/Express endpoint via `baseUrl` in `BuildConfig` or runtime environment variables injected by CI.

Run locally (Gradle):

```pwsh
./gradlew assembleRelease
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb install -r app/build/outputs/apk/release/app-release.apk
```

---

## 21. Observability & Monitoring

- Crashlytics for crash telemetry; attach custom logs and keys.
- Firebase Analytics or Amplitude for event instrumentation (cart additions, successful checkout, scanning events).
- Use Flipper for local debugging and remote database inspection in dev builds.

---

## 22. Accessibility & Internationalization

- Compose semantics & contentDescription for all actionable items.
- Support dynamic font sizes and dark mode.
- Add resource strings for i18n in `strings.xml` or Compose string resources.

---

## 23. Roadmap & Next Steps

- Add ABA PayWay/Payments for production checkout with server-side verification.
- Add order history persistence and admin telemetry.
- Add automated Play Store releases with tracks and feature delivery.

---

## 24. Copilot Prompt Framework (Android)

Use these prompt templates inside VS Code / Copilot for consistent feature generation:

Front-end (Compose) prompt:

```text
Role: Android Developer
Task: Create a Compose screen for [products|cart|checkout|orders]
Details: Use Kotlin + Compose Material3; support screen states (loading, content, error), use ViewModel & StateFlow, include accessibility semantics.
Output: Screen composable and ViewModel with repository calls, tests skeleton.
```

Back-end integration prompt:

```text
Role: Backend Developer (Android caller)
Task: Add a secure endpoint or enhance the proxy `/api/products` to support pagination / search
Details: Keep Node.js / Express proxy pattern; add optional query params and results mapping to client product shape.
Output: Updated server endpoint plus API contract description.
```

---

## 25. Contributing & Workflow

- Follow trunk-based development style with small PRs.
- Keep feature branches, attach screenshot(s) for UI changes, and update `CHANGELOG.md`.
- Add tests for business logic, and e2e acceptance tests for critical flows.

---

If you want, I can scaffold a starter Android project, integrate the Node/Express backend client, configure Hilt/Compose skeletons, and add mobile CI with Play Store publishing. Tell me which step you'd like me to do next.

---

Last updated: November 26, 2025
