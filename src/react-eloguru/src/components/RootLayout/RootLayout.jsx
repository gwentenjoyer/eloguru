import NavBar from "./NavBar";
import {Outlet} from "react-router-dom";
import Footer from "./Footer";

export default function rootLayout({children}) {
    return (
        <>
            <div className="page-container">
                <div className="content-wrap">
                    <NavBar/>
                    <Outlet/>
                </div>
                <Footer/>
            </div>
        </>
        // <>
        //     <NavBar/>
        //     <Outlet/>
        //     <Footer/>
        // </>
    );
}
