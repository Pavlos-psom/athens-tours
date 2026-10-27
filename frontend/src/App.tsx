import {Navigate, Route, Routes} from "react-router";
import RouterLayout from "@/components/RouterLayout.tsx";
import AdminLayout from "@/components/AdminLayout.tsx";
import ProtectedRoute from "@/components/ProtectedRoute.tsx";
import HomePage from "@/pages/HomePage.tsx";
import TourListPage from "@/pages/TourListPage.tsx";
import TourDetailPage from "@/pages/TourDetailPage.tsx";
import GuideListPage from "@/pages/GuideListPage.tsx";
import LoginPage from "@/pages/LoginPage.tsx";
import RegisterPage from "@/pages/RegisterPage.tsx";
import MyBookingsPage from "@/pages/MyBookingsPage.tsx";
import MySchedulePage from "@/pages/MySchedulePage.tsx";
import ForbiddenPage from "@/pages/ForbiddenPage.tsx";
import NotFoundPage from "@/pages/NotFoundPage.tsx";
import AdminToursPage from "@/pages/admin/AdminToursPage.tsx";
import TourFormPage from "@/pages/admin/TourFormPage.tsx";
import AdminGuidesPage from "@/pages/admin/AdminGuidesPage.tsx";
import GuideFormPage from "@/pages/admin/GuideFormPage.tsx";
import AdminSchedulesPage from "@/pages/admin/AdminSchedulesPage.tsx";
import ScheduleFormPage from "@/pages/admin/ScheduleFormPage.tsx";
import AdminBookingsPage from "@/pages/admin/AdminBookingsPage.tsx";

function App() {
  return (
    <Routes>
      <Route element={<RouterLayout/>}>
        {/* Δημόσιες σελίδες */}
        <Route index element={<HomePage/>}/>
        <Route path="tours" element={<TourListPage/>}/>
        <Route path="tours/:uuid" element={<TourDetailPage/>}/>
        <Route path="guides" element={<GuideListPage/>}/>
        <Route path="login" element={<LoginPage/>}/>
        <Route path="register" element={<RegisterPage/>}/>

        {/* Μόνο CUSTOMER */}
        <Route element={<ProtectedRoute allowedRoles={["CUSTOMER"]}/>}>
          <Route path="my-bookings" element={<MyBookingsPage/>}/>
        </Route>

        {/* Μόνο GUIDE */}
        <Route element={<ProtectedRoute allowedRoles={["GUIDE"]}/>}>
          <Route path="my-schedule" element={<MySchedulePage/>}/>
        </Route>

        {/* Μόνο ADMIN */}
        <Route path="admin" element={<ProtectedRoute allowedRoles={["ADMIN"]}/>}>
          <Route element={<AdminLayout/>}>
            <Route index element={<Navigate to="tours" replace/>}/>
            <Route path="tours" element={<AdminToursPage/>}/>
            <Route path="tours/new" element={<TourFormPage/>}/>
            <Route path="tours/:uuid" element={<TourFormPage/>}/>
            <Route path="guides" element={<AdminGuidesPage/>}/>
            <Route path="guides/new" element={<GuideFormPage/>}/>
            <Route path="guides/:uuid" element={<GuideFormPage/>}/>
            <Route path="schedules" element={<AdminSchedulesPage/>}/>
            <Route path="schedules/new" element={<ScheduleFormPage/>}/>
            <Route path="schedules/:uuid" element={<ScheduleFormPage/>}/>
            <Route path="bookings" element={<AdminBookingsPage/>}/>
          </Route>
        </Route>

        <Route path="forbidden" element={<ForbiddenPage/>}/>
        <Route path="*" element={<NotFoundPage/>}/>
      </Route>
    </Routes>
  )
}

export default App
