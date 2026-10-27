import {Link, NavLink} from "react-router";
import {AuthButton} from "@/components/AuthButton.tsx";
import {useAuth} from "@/hooks/useAuth.ts";
import {cn} from "@/lib/utils.ts";

const linkClass = ({isActive}: { isActive: boolean }) =>
  cn("rounded-md px-2 py-1 transition-colors hover:bg-white/10", isActive && "bg-white/20")

const Header = () => {
  const {user} = useAuth();

  return (
    <header className="fixed z-40 w-full bg-aegean text-white shadow">
      <div className="container mx-auto flex flex-wrap items-center justify-between gap-2 px-4 py-3">
        <Link to="/" className="text-xl font-bold tracking-wide">Athens Tours</Link>

        <nav className="flex flex-wrap items-center gap-2 text-sm font-medium">
          <NavLink to="/tours" className={linkClass}>Tours</NavLink>
          <NavLink to="/guides" className={linkClass}>Guides</NavLink>
          {user?.role === "CUSTOMER" && <NavLink to="/my-bookings" className={linkClass}>My Bookings</NavLink>}
          {user?.role === "GUIDE" && <NavLink to="/my-schedule" className={linkClass}>My Schedule</NavLink>}
          {user?.role === "ADMIN" && <NavLink to="/admin" className={linkClass}>Admin</NavLink>}
        </nav>

        <AuthButton/>
      </div>
    </header>
  )
}
export default Header;
