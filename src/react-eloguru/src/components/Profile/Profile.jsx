import React, {useEffect, useState} from "react";
import "../../css/profile.css";

export default function Profile() {

    const [userInfo, setUserInfo] = useState();

    const [isEditMode, setIsEditMode] = useState(false);
    const [isLoading, setIsLoading] = useState(true);

    const fetchCheck = async () => {
        const response = await fetch(`${process.env.REACT_APP_SERVER_URL}/accounts/check`, {credentials: 'include'});
        const data = await response.json();
        console.log(data);
        // setUserInfo(data);

        setUserInfo(
            {
                "idByRole": "3",
                "role": "teacher",
                "userId": "6",
                "email": "myteacher@gmail.com"
            }
        );
        setIsLoading(false);
    };

    useEffect(() => {
            const apiUrl = process.env.REACT_APP_SERVER_URL;
            console.log(apiUrl)
            const fetchUserInfo = async () => {
                const response = await fetch(`${process.env.REACT_APP_SERVER_URL}/accounts/5`, {credentials: 'include'});
                const data = await response.json();
                console.log(data);
                data.fullname="test te"
                setUserInfo(data);
                // {
                //     "surname":"nick"
                //     ,"name":"sur"
                //     ,"id":0,
                //     "categories":["A1"],
                //     "age":0
                // }
                setIsLoading(false);
            };
            fetchUserInfo();
            fetchCheck();
        }, // eslint-disable-next-line
        []);

    const handleEditButton = () => {
        setIsEditMode(!isEditMode);
    }
    const handleLogoutButton = () => {
        fetch(`${process.env.REACT_APP_BASE_URL}/api/v1/logout`, {
            credentials: 'include',
            method: 'POST',
        })
            .then(window.location.href = "/")
            .catch((error) => {
                console.error('Error:', error);
            });
    }

    const formatDate = (date) => {
        const options = {
            weekday: 'long',
            day: '2-digit',
            month: 'long',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit',
            hour12: false
        };

        const formattedDate = new Intl.DateTimeFormat('en-US', options).format(new Date(date));
        return formattedDate;
    }

    const handleUpdateUserData = async () => {
        fetch(`${process.env.REACT_APP_BASE_URL}/student/${userInfo.id}`, {
            credentials: 'include',
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userInfo)
        })
            .then(window.location.reload())
            .catch((error) => {
                console.error('Error:', error);
            });
    }

    const handleCancelButton = (e) => {
        // window.location.reload();
        // e.preventDefault();
        handleEditButton();
        console.log("hi")
    }
    const handleChange = async (event) => {
        const value = event.target.type === 'number' ? parseInt(event.target.value) : event.target.value;
        setUserInfo({
            ...userInfo,
            [event.target.name]: value
        });
    }

    return (
        <div className="container py-5">
            <h1 className="display-6 mb-2 text-center">Мій профіль</h1>
            <h2 className="mb-2 text-center">Власна інформація</h2>
            <div>
                <div className="row g-5 max-size m-auto">
                    <div className="col-lg-6">
                        <div className="position-relative">
                            <div className="p-2" style={{"borderRadius": "30px"}}>
                                {isEditMode ? <></> : <section className="node_category card d-inline-block w-100 mb-3">
                                    <div className="card-body"><h3 className="lead">You are:
                                        {
                                            <span className={"display-6 font-4"}
                                                  style={{"fontSize": "20px"}}>{isLoading ?
                                                <div>Завантаження...</div> : ` ${userInfo.role}`}</span>
                                        }
                                    </h3>
                                    </div>
                                </section>
                                }
                                <section className="node_category card d-inline-block w-100 mb-3">
                                    <div className="card-body"><h3 className="lead">Full name:</h3>
                                        {
                                            isEditMode ? <>
                                                    <input type="text" id="fullname" className="userDataInput"
                                                           style={{
                                                               "width": "100%",
                                                               "background": "#FFFF0044",
                                                               "color": "black",
                                                               "borderColor": "white"
                                                           }}
                                                           placeholder="Edit your full name" name="surname"
                                                           value={userInfo.fullname} onChange={handleChange}
                                                    />
                                                </> :
                                                <span className={"display-6 font-4"}
                                                      style={{"fontSize": "26px"}}>{isLoading ?
                                                    <div>Завантаження...</div> : userInfo.fullname}</span>
                                        }
                                    </div>
                                </section>

                                <section className="node_category card d-inline-block w-100 mb-3">
                                    <div className="card-body"><h3 className="lead">Email:</h3>
                                        {isLoading ? <div>Loading...</div> :
                                            isEditMode ?
                                                <input type="text" id="email" className="userDataInput"
                                                       style={{
                                                           "width": "100%",
                                                           "background": "#FFFF0044",
                                                           "color": "black",
                                                           "borderColor": "white"
                                                       }}
                                                       placeholder="Відредагуйте свій вік" name="email"
                                                       value={userInfo.email} onChange={handleChange}
                                                />
                                                :
                                                <div>
                                                    {userInfo.email}
                                                </div>
                                        }
                                    </div>
                                </section>
                                <section className="node_category card d-inline-block w-100 mb-3">
                                    <div className="card-body"><h3 className="lead">Phone</h3>
                                        {isLoading ? <div>Loading...</div> :
                                            isEditMode ?
                                                <input type="text" id="phone" className="userDataInput"
                                                       style={{
                                                           "width": "100%",
                                                           "background": "#FFFF0044",
                                                           "color": "black",
                                                           "borderColor": "white"
                                                       }}
                                                       placeholder="Edit phone:" name="phone"
                                                       value={userInfo.phone} onChange={handleChange}
                                                />
                                                :
                                                <div>
                                                    {userInfo.phone}
                                                </div>
                                        }
                                    </div>
                                </section>

                            </div>
                        </div>
                    </div>
                    <div className="col-lg-6">
                        <div className="position-relative">
                            <div className="d-flex text-decoration-none text-black p-2">

                                <div className="d-flex flex-column w-100">
                                    <section className="node_category card d-inline-block w-100 mb-3">
                                        <div className="d-flex flex-row w-100"
                                             style={{"padding": "0 0.5rem "}}
                                        >
                                            {
                                                isEditMode ?
                                                    <>

                                                        <div
                                                            className="w-50 bg-warning logout text-center p-2 my-1 d-flex justify-content-center"
                                                            style={{
                                                                "borderRadius": "30px",
                                                                "cursor": "pointer",
                                                                "margin": "0 0.5rem "
                                                            }}
                                                            onClick={handleUpdateUserData}>
                                                            <button className={"btn btn-warning, text-center"}
                                                                    style={{"padding": "0px"}}
                                                                    onClick={handleUpdateUserData}>Save
                                                            </button>
                                                        </div>
                                                        <div
                                                            className="d-flex justify-content-center w-50 bg-danger logout text-center p-2 my-1"

                                                            style={{
                                                                "borderRadius": "30px",
                                                                "cursor": "pointer",
                                                                "margin": "0 0.5rem "
                                                            }}
                                                            onClick={handleCancelButton}>
                                                            <button
                                                                className={isEditMode ? "btn-danger btn" : "btn-warning btn"}
                                                                style={{
                                                                    "borderRadius": "30px",
                                                                    "cursor": "pointer",
                                                                    "margin": "0 0.5rem "
                                                                }}

                                                                onClick={handleCancelButton}>{"Скасувати"}</button>
                                                        </div>
                                                        {/*<div*/}
                                                        {/*    className="d-flex flex-row w-50 bg-danger logout text-center p-2 my-1"*/}
                                                        {/*>*/}
                                                        {/*    <button*/}
                                                        {/*        className={isEditMode ? "btn-danger btn" : "btn-warning btn"}*/}
                                                        {/*        style={{*/}
                                                        {/*            "borderRadius": "30px",*/}
                                                        {/*            "cursor": "pointer",*/}
                                                        {/*            "margin": "0 0.5rem "*/}
                                                        {/*        }}*/}

                                                        {/*        onClick={handleCancelButton}>{"Скасувати"}</button>*/}
                                                        {/*</div>*/}
                                                    </>
                                                    : <>
                                                        <div className="w-50 bg-warning logout text-center p-2 my-1"
                                                             style={{
                                                                 "borderRadius": "30px",
                                                                 "cursor": "pointer",
                                                                 "margin": "0 0.5rem "
                                                             }}
                                                             onClick={handleEditButton}>
                                                            <button className={"btn btn-warning"} style={{"padding": "0px"}}
                                                                    onClick={handleEditButton}>Edit
                                                            </button>
                                                        </div>
                                                        <div className="bg-danger w-50 logout text-center p-2 my-1"
                                                             style={{
                                                                 "borderRadius": "30px",
                                                                 "cursor": "pointer",
                                                                 "margin": "0 0.5rem "
                                                             }}

                                                             onClick={handleLogoutButton}>
                                                            <button className={"btn btn-danger"} style={{"padding": "0px"}}
                                                                    onClick={handleLogoutButton}>Вихід
                                                            </button>
                                                        </div>
                                                    </>

                                            }
                                        </div>
                                    </section>
                                    {isEditMode ? <></> :
                                        <section className="node_category card d-inline-block w-100 mb-3">
                                            <div className="card-body"><h3 className="lead">Account creation date:</h3>
                                                {isLoading ? <div>Loading...</div> :
                                                    <div>
                                                        {formatDate(userInfo.createdDate)}
                                                    </div>
                                                }
                                            </div>
                                        </section>}
                                    {isEditMode ? <></> :
                                        <section className="node_category card d-inline-block w-100 mb-3">
                                            <div className="card-body"><h3 className="lead">Last midified date:</h3>
                                                {isLoading ? <div>Loading...</div> :
                                                    <div>
                                                        {formatDate(userInfo.lastModifiedDate)}
                                                    </div>
                                                }
                                            </div>
                                        </section>}
                                    {isEditMode ? <></> :
                                        <section className="node_category card d-inline-block w-100 mb-3">
                                            <div className="card-body"><h3 className="lead">Privacy and policies</h3>
                                                <ul>
                                                    <li><span><a href="">Data retention summary</a></span>
                                                    </li>
                                                </ul>
                                            </div>
                                        </section>}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
<div className="mb-2 text-center">
<h5>Courses</h5>
</div>
            </div>
        </div>
    );
}
