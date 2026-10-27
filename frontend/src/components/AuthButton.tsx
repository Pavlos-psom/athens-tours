import {useNavigate} from "react-router";
import {toast} from "sonner";
import {useAuth} from "@/hooks/useAuth.ts";
import {Button} from "@/components/ui/button.tsx";
import {Badge} from "@/components/ui/badge.tsx";

export function AuthButton() {
  const {user, isAuthenticated, logoutUser} = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logoutUser();
    toast.success("Logged out");
    navigate("/");
  }

  if (!isAuthenticated || !user) {
    return (
      <div className="flex items-center gap-2">
        <Button variant="ghost" className="text-white hover:bg-white/10 hover:text-white"
                onClick={() => navigate("/login")}>
          Login
        </Button>
        <Button variant="secondary" onClick={() => navigate("/register")}>Register</Button>
      </div>
    )
  }

  return (
    <div className="flex items-center gap-3">
      <span className="hidden text-sm sm:inline">{user.username}</span>
      <Badge variant="outline" className="border-white/40 text-white">{user.role}</Badge>
      <Button variant="secondary" onClick={handleLogout}>Logout</Button>
    </div>
  )
}
