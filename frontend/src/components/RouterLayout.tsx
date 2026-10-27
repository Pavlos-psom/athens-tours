import {Outlet} from "react-router";
import {Toaster} from "sonner";
import Header from "./Header.tsx";
import Footer from "./Footer.tsx";

const RouterLayout = () => {
  return (
    <div>
      <Header/>
      <main className="container mx-auto min-h-[90vh] px-4 pb-12 pt-28">
        <Outlet/>
      </main>
      <Footer/>
      <Toaster position="top-right" richColors/>
    </div>
  )
}
export default RouterLayout;
