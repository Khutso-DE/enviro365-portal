# AI Usage Record

This document records how AI assistance was used during development of the Enviro365 Investments Portal. It is intended to be transparent and should be updated when further AI-assisted changes are made.

## Areas Where AI Assisted

AI assistance was used to:

- Inspect the existing Java project structure and identify compilation and persistence issues.
- Correct repository generic types and add the accessors required by JPA and service code.
- Implement and review the portfolio service and REST controller flow.
- Configure H2 initialization so schema creation occurs before `data.sql` is loaded.
- Create the React, TypeScript, and Vite frontend dashboard.
- Improve onboarding documentation and record current setup limitations.

## Human Review and Validation

AI-generated or AI-suggested changes were reviewed against the project requirements and the existing codebase. Validation included:

- Checking portfolio and withdrawal service behavior through the existing tests.
- Confirming the backend started on port 8080 and returned live portfolio JSON.
- Checking that the frontend request path uses the backend API proxy.
