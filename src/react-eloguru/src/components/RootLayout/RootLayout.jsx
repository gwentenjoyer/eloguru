import NavBar from "./NavBar";
import {Outlet} from "react-router-dom";
import Footer from "./Footer";

export default function rootLayout() {
    return (
        <>
            <NavBar/>
            <Outlet/>
            <Footer/>
        </>
    );
}