import React from "react";
import Slider from "../components/MainPage/Slider";
import AboutUs from "../components/MainPage/AboutUs";
// import AboutUs.jsx from "../components/MainPage/AboutUs.jsx";
// import AboutCourses from "../components/MainPage/AboutCourses";
export default function MainPage(){
    return (
        <>
            <Slider/>
            <AboutUs/>
            {/*<AboutCourses/>*/}
        </>
    );
}