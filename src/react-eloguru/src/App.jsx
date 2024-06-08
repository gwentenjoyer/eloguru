// import './css/App.css';
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import Layout from "./components/RootLayout/RootLayout";
import MainLayout from "./pages/MainLayout";
import RequireAuth from "./hooks/getAuth";
import Profile from './components/Profile/Profile'
import Course from './components/CoursePage/Course'
import './index.css';
import CourseList from "./components/CourseList/CourseList";
import CourseWrapper from "./components/CourseList/CourseWrapper";
import Cookies from 'js-cookie';
import React, { useEffect, useState } from 'react';
import CreateCourse from "./components/CreateCourse/CreateCourse";

const router = createBrowserRouter([
    {
        path: "/",
        element: <Layout/>,
        children: [
            {
                path: "/",
                element: <MainLayout/>
            },
            {
                path: "about",
                element: <div>abba</div>
            },
            {
                path: "mistake",
                element: <div>o</div>
            },
            {
                path: "course/:id",
                element: <CourseWrapper />
            },
            {
                path: "courses",
                element: <CourseList/>
            },
            {
                path: "profile",
                element: <RequireAuth url={"profile"}/>,
                children: [
                    {
                        path: "",
                        element: <Profile/>,
                    },
                ]
            },
            {
                path: "createCourse",
                element: <RequireAuth url={"createCourse"}/>,
                children: [
                    {
                        path: "",
                        element: <CreateCourse/>,
                    },
                ]
            },

        ],
        errorElement: <div>ooo criko criko</div>
    },
]);

export default function App() {
    return (
        <RouterProvider router={router}/>
    )
};