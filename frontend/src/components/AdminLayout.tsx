import {NavLink, Outlet} from "react-router";
import {cn} from "@/lib/utils.ts";

const tabs = [
  {to: "/admin/tours", label: "Tours"},
  {to: "/admin/guides", label: "Guides"},
  {to: "/admin/schedules", label: "Schedules"},
  {to: "/admin/bookings", label: "Bookings"},
]

const AdminLayout = () => {
  return (
    <div>
      <div className="mb-6 flex flex-wrap gap-2 border-b pb-3">
        {tabs.map((tab) => (
          <NavLink
            key={tab.to}
            to={tab.to}
            className={({isActive}) =>
              cn("rounded-4xl px-4 py-1.5 text-sm font-medium hover:bg-muted", isActive && "bg-primary text-primary-foreground hover:bg-primary")
            }
          >
            {tab.label}
          </NavLink>
        ))}
      </div>
      <Outlet/>
    </div>
  )
}
export default AdminLayout;
