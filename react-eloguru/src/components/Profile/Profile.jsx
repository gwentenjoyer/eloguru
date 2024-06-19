import React, { useEffect, useState } from "react";
import "../../css/profile.css";
import { useNavigate } from "react-router-dom";
import CoursePreview from "./CoursePreview";
import axios from "axios";
import { Container } from "react-bootstrap";

export default function Profile() {
    const [userInfo, setUserInfo] = useState();
    const [isEditMode, setIsEditMode] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const navigate = useNavigate();
    const [courses, setCourses] = useState([]);
    const [accountId, setAccountId] = useState();

    const fetchCourse = async (id) => {
        const response = await fetch(`${process.env.REACT_APP_BASE_URL}/courses/${id}`, { credentials: 'include' });
        if (!response.ok) {
            console.error("Failed to fetch data to get course");
            return;
        }
        const data = await response.json();
        return data;
    };

    const fetchCourseProgress = async (id) => {
        const response = await fetch(`${process.env.REACT_APP_BASE_URL}/courses/${id}/getProgress`, { credentials: 'include' });
        if (!response.ok) {
            console.error("Failed to fetch data to get course");
            return;
        }
        const data = await response.json();
        return data;
    };

    const fetchUserCheck = async () => {
        const response = await fetch(`${process.env.REACT_APP_BASE_URL}/accounts/check`, { credentials: 'include' });
        if (!response.ok) {
            console.error("Failed to fetch data to check user");
            return;
        }
        const data = await response.json();
        return data;
    };

    useEffect(() => {
        const fetchUserInfo = async () => {
            setIsLoading(true);
            try {
                const [userInfoResponse, checkResponse] = await Promise.all([
                    fetch(`${process.env.REACT_APP_BASE_URL}/accounts/getUserInfo`, { credentials: 'include' }),
                    fetchUserCheck()
                ]);

                if (!userInfoResponse.ok) {
                    console.error("Failed to fetch user data");
                    window.location.href = "/";
                }

                const data = await userInfoResponse.json();
                setUserInfo(data);
                setAccountId(checkResponse.userId);
                setIsLoading(false);
            } catch (error) {
                console.error("Error fetching user info:", error);
                window.location.href = "/";
            }
        };

        fetchUserInfo();
    }, []);

    useEffect(() => {
        const fetchAllCourses = async () => {
            if (userInfo) {
                let coursePromises;
                if (userInfo.role === "TEACHER" && userInfo.teachingCoursesId) {
                    coursePromises = userInfo.teachingCoursesId.map(id => id !== -1 && fetchCourse(id));
                } else if (userInfo.role === "STUDENT" && userInfo.coursesId) {
                    coursePromises = userInfo.coursesId.map(id => id !== -1 && fetchCourse(id));
                }

                if (coursePromises) {
                    const courseData = await Promise.all(coursePromises);
                    const filteredCourses = courseData.filter(course => course !== null && course.id !== undefined);
                    setCourses(filteredCourses);
                }
            }
        };

        fetchAllCourses();
    }, [userInfo]);

    const handleEditButton = () => {
        setIsEditMode(!isEditMode);
    };

    const handleLogoutButton = () => {
        fetch(`${process.env.REACT_APP_BASE_URL}/accounts/logout`, {
            credentials: 'include',
            method: 'POST',
        })
            .then(() => window.location.href = "/")
            .catch((error) => {
                console.error('Error:', error);
            });
    };

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
        return date ? new Intl.DateTimeFormat('en-US', options).format(new Date(date)) : null;
    };

    const handleUpdateUserData = async () => {
        const payload = {
            email: userInfo.email,
            phone: userInfo.phone,
            country: userInfo.country,
            fullname: userInfo.fullname
        };

        axios.put(`${process.env.REACT_APP_BASE_URL}/accounts`, payload, { withCredentials: true })
            .then((res) => {
                if (res.status === 200) {
                    setIsEditMode(false);
                }
            })
            .catch((error) => console.error('Error:', error));
    };

    const handleCancelButton = () => {
        handleEditButton();
    };

    const handleChange = (event) => {
        const value = event.target.type === 'number' ? parseInt(event.target.value) : event.target.value;
        setUserInfo({
            ...userInfo,
            [event.target.name]: value
        });
    };

    return (
        <div className="container py-5">
            <h1 className="display-6 mb-2 text-center">My profile</h1>
            <h2 className="mb-2 text-center">Account information</h2>
            <div>
                <div className="row g-5 max-size m-auto">
                    <div className="col-lg-6">
                        <div className="position-relative">
                            <div className="p-2" style={{borderRadius: "30px"}}>
                                {!isEditMode && (
                                    <section className="node_category card d-inline-block w-100 mb-3">
                                        <div className="card-body">
                                            <h3 className="lead">You are:
                                                <span className="display-6 font-4" style={{fontSize: "20px"}}>
                                                    {isLoading ? <div>Loading...</div> : ` ${userInfo?.role}`}
                                                </span>
                                            </h3>
                                        </div>
                                    </section>
                                )}
                                <section className="node_category card d-inline-block w-100 mb-3">
                                    <div className="card-body">
                                        <h3 className="lead">Full name:</h3>
                                        {isEditMode ? (
                                            <input
                                                type="text"
                                                id="fullname"
                                                className="userDataInput"
                                                style={{
                                                    width: "100%",
                                                    background: "#FFFF0044",
                                                    color: "black",
                                                    borderColor: "white"
                                                }}
                                                placeholder="Edit your full name"
                                                name="fullname"
                                                value={userInfo.fullname}
                                                onChange={handleChange}
                                            />
                                        ) : (
                                            <span className="display-6 font-4" style={{fontSize: "26px"}}>
                                                {isLoading ? <div>Loading...</div> : userInfo?.fullname}
                                            </span>
                                        )}
                                    </div>
                                </section>

                                <section className="node_category card d-inline-block w-100 mb-3">
                                    <div className="card-body">
                                        <h3 className="lead">Email:</h3>
                                        {isLoading ? <div>Loading...</div> : isEditMode ? (
                                            <input
                                                type="text"
                                                id="email"
                                                className="userDataInput"
                                                style={{
                                                    width: "100%",
                                                    background: "#FFFF0044",
                                                    color: "black",
                                                    borderColor: "white"
                                                }}
                                                placeholder="Edit your email"
                                                name="email"
                                                value={userInfo?.email}
                                                onChange={handleChange}
                                            />
                                        ) : (
                                            <div>{userInfo?.email}</div>
                                        )}
                                    </div>
                                </section>
                                <section className="node_category card d-inline-block w-100 mb-3">
                                    <div className="card-body">
                                        <h3 className="lead">Phone:</h3>
                                        {isLoading ? <div>Loading...</div> : isEditMode ? (
                                            <input
                                                type="tel"
                                                id="phone"
                                                className="userDataInput"
                                                style={{
                                                    width: "100%",
                                                    background: "#FFFF0044",
                                                    color: "black",
                                                    borderColor: "white"
                                                }}
                                                pattern="\+[0-9]{10}"
                                                placeholder="Edit phone"
                                                name="phone"
                                                value={userInfo?.phone}
                                                onChange={handleChange}
                                            />
                                        ) : (
                                            <div>{userInfo?.phone}</div>
                                        )}
                                    </div>
                                </section>
                                <section className="node_category card d-inline-block w-100 mb-3">
                                    <div className="card-body">
                                        <h3 className="lead">Country:</h3>
                                        {isLoading ? <div>Loading...</div> : isEditMode ? (
                                            <input
                                                type="tel"
                                                id="country"
                                                className="userDataInput"
                                                style={{
                                                    width: "100%",
                                                    background: "#FFFF0044",
                                                    color: "black",
                                                    borderColor: "white"
                                                }}
                                                pattern="\+[0-9]{10}"
                                                placeholder="Edit country"
                                                name="country"
                                                value={userInfo?.country}
                                                onChange={handleChange}
                                            />
                                        ) : (
                                            <div>{userInfo?.country}</div>
                                        )}
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
                                        <div className="d-flex flex-row w-100" style={{padding: "0 0.5rem "}}>
                                            {isEditMode ? (
                                                <Container className="d-flex justify-content-center">
                                                    <button
                                                        className="w-50 bg-success logout text-center p-2 my-1"
                                                        style={{
                                                            borderRadius: "30px",
                                                            cursor: "pointer",
                                                            margin: "0 0.5rem ",
                                                            color: "white",
                                                            borderColor: "transparent"
                                                        }}
                                                        onClick={handleUpdateUserData}
                                                    >
                                                        Save
                                                    </button>
                                                    <button
                                                        className="bg-danger w-50 logout text-center p-2 my-1"
                                                        style={{
                                                            borderRadius: "30px",
                                                            cursor: "pointer",
                                                            margin: "0 0.5rem ",
                                                            borderColor: "transparent",
                                                            color: "white"
                                                        }}
                                                        onClick={handleCancelButton}
                                                    >
                                                        Discard
                                                    </button>
                                                </Container>
                                            ) : (
                                                <Container className="d-flex justify-content-center">
                                                    <div
                                                        className="w-50 bg-warning logout text-center p-2 my-1"
                                                        style={{
                                                            borderRadius: "30px",
                                                            cursor: "pointer",
                                                            margin: "0 0.5rem "
                                                        }}
                                                        onClick={handleEditButton}
                                                    >
                                                        <button className="btn btn-warning" style={{ padding: "0px" }}>
                                                            Edit profile
                                                        </button>
                                                    </div>
                                                </Container>
                                            )}
                                        </div>
                                    </section>
                                    {!isEditMode && (
                                        <>
                                            <section className="node_category card d-inline-block w-100 mb-3">
                                                <div className="card-body">
                                                    <h3 className="lead">Account creation date:</h3>
                                                    {isLoading ? <div>Loading...</div> : (
                                                        <div>{formatDate(userInfo.createdDate)}</div>
                                                    )}
                                                </div>
                                            </section>
                                            <section className="node_category card d-inline-block w-100 mb-3">
                                                <div className="card-body">
                                                    <h3 className="lead">Last modified date:</h3>
                                                    {isLoading ? <div>Loading...</div> : (
                                                        <div>{formatDate(userInfo.lastModifiedDate)}</div>
                                                    )}
                                                </div>
                                            </section>
                                            <section className="node_category card d-inline-block w-100 mb-3">
                                                <div className="card-body">
                                                    <h3 className="lead">Privacy and policies</h3>
                                                    <ul>
                                                        <li><span><a href="">Data retention summary</a></span></li>
                                                    </ul>
                                                </div>
                                            </section>
                                        </>
                                    )}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                {isLoading ? (
                    <div>Loading...</div>
                ) : (
                    <div className="mb-2 text-center">
                        {userInfo?.role !== "ADMIN" && !isEditMode && (
                            <h5>Courses you're {userInfo.role === "TEACHER" ? "teaching" : "studying"}:</h5>
                        )}
                        {!isEditMode && (
                            <div className="d-flex flex-column justify-content-center text-center align-items-center">
                                {courses.length > 0 ? (
                                    courses.sort((a, b) => a.id - b.id).map((course, index) => (
                                        <CoursePreview key={index} label={course.header} id={course.id} />
                                    ))
                                ) : (
                                    userInfo.role !== "ADMIN" && (
                                        userInfo.role === "TEACHER" ?
                                            <p>You don't teach any courses.</p> :
                                            <p>You don't study any course. Go to <a href={'/course'}>courses</a> to find one.</p>
                                    )
                                )}
                            </div>
                        )}
                    </div>
                )}
            </div>
        </div>
    );
}
