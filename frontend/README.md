# Frontend

This project was generated using [Angular CLI](https://github.com/angular/angular-cli) version 21.2.11.

## Runtime workflow

- In local development the frontend talks to the real backend API.
- In production the backend is treated as read-only for mutating endpoints.
- Create/update/delete flows are simulated in the browser through `sessionStorage`.
- The mock state is tied to the browser session and is cleared when the tab/browser session ends.

## Development server

To start a local development server, run:

```bash
ng serve
```

Once the server is running, open your browser and navigate to `http://localhost:4200/`. The application will automatically reload whenever you modify any of the source files.

## Production behavior

When deployed, the app keeps read requests against the backend, but write requests are redirected to the session sandbox. This means:

- users can keep testing without affecting the shared database
- a new browser session starts from a clean state
- closing the browser/tab removes the mock data stored in `sessionStorage`

## Application areas

The Angular routes are lazy-loaded from `src/app/app.routes.ts`:

- public catalog and discovery: `/home`, `/film`, `/programmazione`, `/sale`, `/about`;
- seat selection and ticket display: `/programmazione/:programmazioneId/sala` and `/ticket-show/:numeroOrdine`;
- authenticated orders: `/ordini`;
- admin film and programming management: `/admin/film/**` and `/admin/programmazione`.

The `userAuthGuard` and `adminAuthGuard` protect the authenticated areas. In production, `AuthService`, `OrdineService`, `FilmManagementService` and `AdminProgrammazioneManagementService` can use `SessionSandboxService` for session-scoped writes, while catalog reads continue to use the backend API. The sandbox stores mock users, orders, film/programming changes and HTTP cache entries in `sessionStorage`; it is not a persistent database.

The API base URL is selected by the environment files: local development points to `http://localhost:8080`, while the production build points to the deployed backend URL. Run `npm start` for the local Angular server and `npm run build` for the production build.

## Code scaffolding

Angular CLI includes powerful code scaffolding tools. To generate a new component, run:

```bash
ng generate component component-name
```

For a complete list of available schematics (such as `components`, `directives`, or `pipes`), run:

```bash
ng generate --help
```

## Building

To build the project run:

```bash
ng build
```

This will compile your project and store the build artifacts in the `dist/` directory. By default, the production build optimizes your application for performance and speed.

## Running unit tests

To execute unit tests with the [Vitest](https://vitest.dev/) test runner, use the following command:

```bash
ng test
```

## Running end-to-end tests

For end-to-end (e2e) testing, run:

```bash
ng e2e
```

Angular CLI does not come with an end-to-end testing framework by default. You can choose one that suits your needs.

## Additional Resources

For more information on using the Angular CLI, including detailed command references, visit the [Angular CLI Overview and Command Reference](https://angular.dev/tools/cli) page.
