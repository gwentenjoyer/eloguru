import React, {useEffect, useState} from "react";
import {Button, Navbar, Container} from "react-bootstrap";
import {NavLink, Link} from "react-router-dom";
import "../../css/Navbar.css"
import LoginModal from '../Login/LoginModal'
// import getRefreshTokens.js from "../hooks/getRefreshTokens.js";
import { useNavigate, useSearchParams } from "react-router-dom";
import Cookies from "js-cookie";
import getRefreshTokens from "../../hooks/getRefreshTokens";
import axios from "axios";
import SearchBar from "./SearchBar";
// import handleLogout from "../../hooks/handleLogout";

export default function NavBar() {

    const [userInfo, setUserInfo] = useState(302);
    const [userInfoData, setUserInfoData] = useState(302);
    const [isLoading, setIsLoading] = useState(true);
    const [modalShow, setModalShow] = React.useState(false);
    const [searchParams] = useSearchParams();

    const navigate = useNavigate();

    const checkAccessToken = () => {
        const token = Cookies.get('accessToken');
        console.log(!!token)
        return !!token;
    };

    const handleLogout = async () => {
         fetch(`/accounts/logout`, {
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
                    const data = await fetch(`/accounts/check`, {credentials: 'include'});
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
                    // console.log(await data.json())
                    if (!data.ok) {
                        return;
                    }

                    if (!data.redirected) {
                        setUserInfo(data);
                    // } else if (await getRefreshTokens() === true) {
                    //     setUserInfo(data);
                        setUserInfoData(await data.json())
                    }
                    if (await data.text() === "admin") {
                        setIsLoading(false);
                    }
                }
                catch {
                }
            };
            fetchUserInfo();
        if (searchParams.get('login') === 'true') {
                setModalShow(true);
            }
        }, [searchParams]);

    return (
        <header>
            <Navbar className="navbar navbar-expand-lg navbar-light sticky-top p-0 navbar-blu" variant="dark"
                    expand="lg">
                <Container>
                {/*<div className={"d-flex flex-row justify-content-around w-100"}>*/}

                    <NavLink className={"py-4 pr-lg-5 me-2 align-items-center l-height navbar-brand"} to="/">
                        <h2 className={"me-0 fa logo-text font-6 brandname-first-e"} ><i className="fa fa-solid fa-user-graduate text-white me-2 brandname-first-e"></i><span id="brandname-first-e">E</span>loguru
                        </h2></NavLink>
                    <div className={"ms-auto"}>
                        <Navbar.Toggle className={"ms-auto"} aria-controls="basic-navbar-nav"/>
                    </div>
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Container className={"p-0"}>
                            <div className="navbar-nav p-lg-0">
                                <div className="d-flex align-items-center m-1">

                                    <NavLink className={"nav-item nav-link"} to="/course">Courses</NavLink>
                                </div>
                                <div className="d-flex align-items-center m-1">
                                    <NavLink className={"nav-item nav-link"} to="/about">About</NavLink>
                                </div>
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
                        {userInfoData?.role == "teacher" &&
                            <Button variant="primary" className="m-2 button-login" onClick={() => navigate(`/createCourse`)}>
                                Create
                            </Button>
                        }

                        {userInfoData?.role == "admin" &&
                            <Button variant="primary" className="m-2 button-login" onClick={() => navigate(`/admin/data`)}>
                                Administration
                            </Button>
                        }

                        {/*{ checkAccessToken()?*/}

                            {/*:*/}

                        {/*    <Button variant="primary" onClick={() => setModalShow(true)}>*/}
                        {/*        Log in*/}
                        {/*    </Button>*/}
                        {/*}*/}
                        {/*<Button variant="primary" className="button-login" onClick={() => setModalShow(true)}>*/}
                        {/*    Log in*/}
                        {/*</Button>*/}
                        {userInfo.status === 200 ?
                            <>
                                <Button variant="primary" className="m-2 button-login" onClick={() => navigate(`/profile`)}>
                                    Profile
                                </Button>
                                <Button variant="primary" className="button-login m-2" onClick={() => handleLogout()}>
                                    Log out
                                </Button>
                            </>
                            :
                            <Button variant="primary" className="button-login" onClick={() => setModalShow(true)}>
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
