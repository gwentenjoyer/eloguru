import React, {useEffect, useState} from "react";
import {Button, Navbar, Container} from "react-bootstrap";
import {NavLink, Link} from "react-router-dom";
import "../../css/Navbar.css"
import LoginModal from '../Login/LoginModal'
// import getRefreshTokens.js from "../hooks/getRefreshTokens.js";
import { useNavigate } from "react-router-dom";
import Cookies from "js-cookie";
import getRefreshTokens from "../../hooks/getRefreshTokens";
import axios from "axios";
import SearchBar from "./SearchBar";
// import handleLogout from "../../hooks/handleLogout";

export default function NavBar() {

    const [userInfo, setUserInfo] = useState(302);
    const [isLoading, setIsLoading] = useState(true);
    const [modalShow, setModalShow] = React.useState(false);

    const navigate = useNavigate();

    const checkAccessToken = () => {
        const token = Cookies.get('accessToken');
        console.log(!!token)
        return !!token;
    };

    const handleLogout = async () => {
         fetch(`${process.env.REACT_APP_SERVER_URL}/accounts/logout`, {
            credentials: 'include',
            method: 'POST',
        })
            .then(() => {
                localStorage.clear();
                window.location.href = "/"})
            .catch((error) => {
                console.error('Error:', error);
            });

    };

    useEffect(() => {
            const fetchUserInfo = async () => {
                try {
                    const data = await fetch(`${process.env.REACT_APP_SERVER_URL}/accounts/check`, {credentials: 'include'});
                    // const data = await response?.json();
                    // if (!response?.redirected) {
                    //     setUserInfo(data);
                    // }
                    // // else if (await getRefreshTokens.js() === true) {
                    // //     setUserInfo(data);
                    // // }
                    // if (data?.role === "admin") {
                    //     setIsLoading(false);
                    // }
                    if (!data.ok) {
                        if (data.status === 401) {
                            console.log("Unauthorized. Please log in.");
                        } else if (data.status === 400) {
                            console.log("Bad request.");
                        } else {
                            console.log("An error occurred:", data.statusText);
                        }
                        return;
                    }

                    if (!data.redirected) {
                        setUserInfo(data);
                    } else if (await getRefreshTokens() === true) {
                        setUserInfo(data);
                    }
                    if (await data.text() === "admin") {
                        setIsLoading(false);
                    }
                }
                catch {
                    console.log("unlogined")
                }
            };
            fetchUserInfo();
        }, []);

    return (
        <header>
            <Navbar className="navbar navbar-expand-lg navbar-light sticky-top p-0 navbar-blu" variant="dark"
                    expand="lg">
                <Container>
                    <NavLink className={"py-4 px-lg-5 me-2 align-items-center l-height navbar-brand"} to="/">
                        <h2 className={"me-0 fa logo-text font-6"}><i className="fa fa-solid fa-user-graduate text-white me-2"></i><span id="brandname-first-e">E</span>loguru
                        </h2></NavLink>
                    <div className={"ms-auto"}>
                        <Navbar.Toggle className={"ms-auto"} aria-controls="basic-navbar-nav"/>
                    </div>
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Container className={"p-0"}>
                            <div className="navbar-nav p-lg-0">
                                <NavLink className={"nav-item nav-link"} to="/courses">Курси</NavLink>
                                <NavLink className={"nav-item nav-link"} to="/about">Про нас</NavLink>
                                {/*{!isLoading ?*/}
                                {/*    <NavLink className={"nav-item nav-link"} to="/admin">Адміністрування</NavLink>*/}
                                {/*    :*/}
                                {/*    <></>*/}
                                {/*}*/}
                                <SearchBar></SearchBar>
                            </div>
                        </Container>
                    </Navbar.Collapse>
                    {/*<div className={"ps-2 mx-2"}>*/}
                    {/*    {userInfo.status === 200 ?*/}
                    {/*        <Link to={"/profile"}>*/}
                    {/*            <Button variant={"light"}*/}
                    {/*                    className={"loggin-btn btn btn-warning px-lg-5 btn"}*/}
                    {/*            >Профіль</Button>*/}
                    {/*        </Link>*/}
                    {/*        :*/}
                    {/*        <Link to={"/login"}>*/}
                    {/*            <Button variant={"light"}*/}
                    {/*                    className={"loggin-btn btn btn-warning px-lg-5 btn"}*/}
                    {/*            >Авторизація</Button>*/}
                    {/*        </Link>*/}
                    {/*    }*/}
                    {/*</div>*/}
                    {/*<ModalTriggerButton/>*/}
                    <>
                        <Button variant="primary" onClick={() => navigate(`/CreateCourse`)}>
                            Create
                        </Button>

                        {/*{ checkAccessToken()?*/}

                            {/*:*/}

                        {/*    <Button variant="primary" onClick={() => setModalShow(true)}>*/}
                        {/*        Log in*/}
                        {/*    </Button>*/}
                        {/*}*/}
                        <Button variant="primary" onClick={() => setModalShow(true)}>
                            Log in
                        </Button>
                        {userInfo.status === 200 ?
                            <>
                            <Button variant="primary" onClick={() => navigate(`/profile`)}>
                                Profile
                            </Button>
                            <Button variant="primary" onClick={() => handleLogout()}>
                                Log out
                            </Button>
                            </>
                            :
                            <Button variant="primary" onClick={() => setModalShow(true)}>
                                Log in
                            </Button>
                        }
                        <LoginModal
                            show={modalShow}
                            onHide={() => setModalShow(false)}
                        />
                    </>
                </Container>
            </Navbar>
        </header>
    );
}
