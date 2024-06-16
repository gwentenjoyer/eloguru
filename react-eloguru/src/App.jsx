// import './css/App.css';
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import Layout from "./components/RootLayout/RootLayout";
import MainLayout from "./pages/MainLayout";
import RequireAuth from "./hooks/getAuth";
import Profile from './components/Profile/Profile'
import './index.css';
import CourseList from "./components/CourseList/CourseList";
import CourseWrapper from "./components/CourseList/CourseWrapper";
import CreateCourse from "./components/CreateCourse/CreateCourse";
import ErrorPage from "./components/RootLayout/ErrorPage";
import AboutPage from "./components/MainPage/AboutPage";
import RegisterActive from "./pages/RegisterActive";
import DataAdminPage from "./components/CreateCourse/DataAdminPage";
import AdminPage from "./pages/adminPage";

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
                element: <AboutPage/>
            },
            {
                path: "mistake",
                element: <ErrorPage />
            },
            {
                path: "course/:id",
                element: <CourseWrapper />
            },
            {
                path: "course",
                element: <CourseList/>
            },
            {
                path: "activate",
                element: <RegisterActive/>

            },
            {
                path: "admin",
                element: <RequireAuth url={"admin"}/>,
                children: [
                    {
                        path: "",
                        element: <AdminPage/>
                    },
                    {
                        path: "",
                        // element: <RequireAuth url={"admin/data"}/>,
                        children: [
                            {
                                path: "data",
                                element: <DataAdminPage/>,
                            }
                        ]
                    },
                ]
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
        errorElement: <ErrorPage />
    },
]);

export default function App() {
    return (
        <RouterProvider router={router}/>
    )
};
