// import './css/App.css';
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import Layout from "./components/RootLayout/RootLayout";
import MainLayout from "./pages/MainLayout";
import RequireAuth from "./hooks/getAuth";
import Profile from './components/Profile/Profile'
import Course from './components/CoursePage/Course'
import './index.css';

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
                path: "course",
                element: <Course/>
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

        ],
        errorElement: <div>ooo criko criko</div>
    },
]);

export default function App() {
    return (
        <RouterProvider router={router}/>
    )
};