# fixitnow_A_Local_Maintenance_Request_System_2025_2026
title :FixItNow - A Local Maintenance Request System FixItNow is a maintenance request management system that enables residents to report infrastructure issues (e.g., plumbing, electrical failures) and allows service providers to accept and resolve them. Administrators oversee service provider management and job assignments.

🚀 Features 1️⃣ Maintenance Request Management (CRUD) ✔️ Submit Request – Residents can report a maintenance issue (e.g., plumbing, electrical). ✔️ Update Request – Residents can modify request details before assignment. ✔️ Delete Request – Residents can cancel their request if no technician is assigned. ✔️ Track Request Status – View request progress (Pending, In Progress, Completed).

2️⃣ Service Provider Management (CRUD) ✔️ Admin Adds Providers – Admins can register service providers (technicians, electricians, plumbers). ✔️ Update Provider Information – Admins can update provider details. ✔️ Remove Provider – Admins can deactivate or remove a provider. ✔️ Job Assignment – Providers can accept/reject maintenance requests.

3️⃣ Authentication & Authorization ✔️ User Authentication – Secure signup, login, logout, and account deletion. ✔️ Role-Based Authorization:

Residents – Submit and track maintenance requests. Service Providers – View and manage assigned jobs. Admins – Oversee providers and approve/reject jobs. 🌐 Backend (REST API - Local Only) ✔️ APIs for user authentication, request handling, and provider management. ✔️ Role-based access control (RBAC) for different user roles. ✔️ Runs locally (No Firebase/Firestore).

✅ Testing Strategy ✔️ Widget Testing – UI elements for request submission and tracking. ✔️ Unit Testing – Business logic (Bloc functions for request state management). ✔️ Integration Testing – End-to-end API interaction for request updates.

🛠 Architecture FixItNow follows Domain-Driven Design (DDD) and the Bloc architecture:

Presentation Layer: UI components (Widgets, States, and Events). Application Layer: Business logic handled with Bloc Use Cases. Domain Layer: Core logic with Entities & Validation. Infrastructure Layer: Remote Data Source: REST API

Group Members
Name	Student ID
Abenezer Elias	UGR/3180/15
Blen Gebre	UGR/0847/15
Henok Yonas	UGR/4016/15
Samuel Teferi	UGR/1535/15
Yeabsira Yibeltal	UGR/5562/15
