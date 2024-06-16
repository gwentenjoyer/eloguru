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
                path: "courses",
                element: <CourseList/>
            },
            {
                path: "activate",
                element: <RegisterActive/>

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
