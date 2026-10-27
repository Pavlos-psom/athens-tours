import {Navigate, Outlet, useLocation} from "react-router";
import {useAuth} from "@/hooks/useAuth.ts";
import type {Role} from "@/types.ts";

type Props = {
  allowedRoles?: Role[]; // αν λείπει: αρκεί να είναι συνδεδεμένος
}

const ProtectedRoute = ({allowedRoles}: Props) => {
  const {isAuthenticated, user} = useAuth();
  const location = useLocation();

  // 1) Δεν είναι συνδεδεμένος -> login (θυμόμαστε από πού ήρθε)
  if (!isAuthenticated || !user) {
    return <Navigate to="/login" state={{from: location}} replace/>;
  }

  // 2) Είναι συνδεδεμένος αλλά με λάθος ρόλο -> forbidden
  if (allowedRoles && !allowedRoles.includes(user.role)) {
    return <Navigate to="/forbidden" replace/>;
  }

  // 3) Όλα ΟΚ -> δείξε τις child routes
  return <Outlet/>
}

export default ProtectedRoute;
