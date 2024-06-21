import React, { useEffect, useState } from 'react';
import '../../css/CourseDetailPage.css';
import Collapse from "./Collapse";
import Comment from "./Comment";
import axios from "axios";
import Rating from '@mui/material/Rating';
import { useNavigate } from "react-router-dom";
import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";
import DataAdminPage from "../CreateCourse/DataAdminPage";
import DeleteCourseVerify from "./DeleteCourseVerify";
import LoginModal from "../Login/LoginModal";
import TopicCreateModal from "../TopicCreate/TopicCreateModal";

const Course = ({ courseId }) => {
    const [activeTab, setActiveTab] = useState('info');
    const [topics, setTopics] = useState([]);
    const [course, setCourse] = useState();
    const [isLoading, setIsLoading] = useState(true);
    const [isEditMode, setIsEditMode] = useState(false);
    const [verifyDelete, setVerifyDelete] = useState(false);
    const [userRole, setUserRole] = useState('');
    const [topicModalShow, setTopicModalShow] = useState(false);

    const [courseName, setCourseName] = useState('');
    const [description, setDescription] = useState('');
    const [durationDays, setDurationDays] = useState('');
    const [startDate, setStartDate] = useState('');
    const [category, setCategory] = useState('');
    const [comment, setComment] = useState('');
    const [commentRate, setCommentRate] = useState('');
    const [userEnrolled, setUserEnrolled] = useState(false);
    const navigate = useNavigate();
    const [teacherName, setTeacherName] = useState('');
    const [triggerFetch, setTriggerFetch] = useState(false);
    const [isTeacherOwn, setIsTeacherOwn] = useState(false);

    const fetchEnrollmentStatus = async () => {
        try {
            const response = await fetch(`${process.env.REACT_APP_BASE_URL}/courses/${courseId}/checkEnroll`, {
                credentials: 'include'
            });
            if (response.ok) {
                const isEnrolled = await response.json();
                console.log("enrolstat", isEnrolled)
                setUserEnrolled(isEnrolled);
            } else {
                console.error('Failed to fetch enrollment status');
            }
        } catch (error) {
            console.error('Error fetching enrollment status:', error);
        }
    };
    const fetchTeacherStatus = async () => {
        try {
            const response = await fetch(`${process.env.REACT_APP_BASE_URL}/courses/${courseId}/isTeacherOwn`, {
                credentials: 'include'
            });
            if (response.ok) {
                const isEnrolled = await response.json();
                setIsTeacherOwn(isEnrolled);
            } else {
                console.error('Failed to fetch own status');
            }
        } catch (error) {
            console.error('Error fetching owning status:', error);
        }
    };

    useEffect(() => {
        const fetchUserInfo = async () => {
            try {
                const [courseResponse, topicsResponse] = await Promise.all([
                    fetch(`${process.env.REACT_APP_BASE_URL}/courses/${courseId}`, { credentials: 'include' }),
                    fetchTopics()
                ]);

                if (!courseResponse.ok) {
                    throw new Error(`Failed to fetch course data: ${courseResponse.statusText}`);
                }

                const data = await courseResponse.json();
                console.log("course data", data)
                setCourse(data);
                const teacherNameRes = await fetch(`${process.env.REACT_APP_BASE_URL}/accounts/teacher/${data.teacherId}/getName`, { credentials: 'include' });
                setTeacherName(await teacherNameRes.text())
                setTopics(await topicsResponse.json())
                setCourseName(data?.header)
                setDescription(data?.description)
                setDurationDays(data?.durationDays)
                setStartDate(data?.startDate ? data?.startDate?.slice(0, 10) : '')
                setCategory(data?.categories)
                const role = await getUserRole();
                if (role === "STUDENT")
                    await fetchEnrollmentStatus();
                else if (role === "TEACHER")
                    await fetchTeacherStatus();
                setIsLoading(false);

            } catch (error) {
                console.error("Error fetching course and topics:", error);
                throw error;
            }
        };

        fetchUserInfo();
    }, [courseId, triggerFetch]);

    const fetchTopics = async () => {
        const response = await fetch(`${process.env.REACT_APP_BASE_URL}/courses/${courseId}/topics`, { credentials: 'include' });
        console.log(response)
        return response;
    }

    const getUserRole = async () => {
        const response = await fetch(`${process.env.REACT_APP_BASE_URL}/accounts/check`, { credentials: 'include' });
        let role = '';
        if (response.status == 200) {
            role = (await response.json()).role.toString().toUpperCase();
            setUserRole(role);
        }
        return role;
    }

    const handleEnroll = async () => {
        const response = await fetch(`${process.env.REACT_APP_BASE_URL}/courses/${courseId}/enroll`, { credentials: 'include', method: "POST" });

        if (!response.status === 200) {
            console.log(response)
            console.error("Failed to enroll")
        }
        console.log("enrolled")
        setUserEnrolled(true);
    };

    const handleDisenroll = async () => {
        const response = await fetch(`${process.env.REACT_APP_BASE_URL}/courses/${courseId}/disenroll`, { credentials: 'include', method: "POST" });
        console.log("another", response)
        if (response.status !== 200) {
            console.log(response)
            console.error("Failed to disenroll")
        }
        if (response.status === 200) {
            console.log("disenrolled")
            setUserEnrolled(false);
        }
    };

    const handleEditButton = () => {
        setActiveTab('info')
        setIsEditMode(!isEditMode);
    }

    const handleCommentCancel = () => {
        setComment("");
        setCommentRate(null);
    }

    const handleCommentSave = async () => {
        const payload = {
            courseId,
            text: comment,
            rating: commentRate
        }
        try {
            const response = await fetch(`${process.env.REACT_APP_BASE_URL}/feedbacks`, {
                credentials: 'include',
                method: "POST",
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(payload)
            });
            if (response.status === 201 || response.status === 200) {
                setComment("");
                setCommentRate(null);
                console.log("successfully created comment");
                setTriggerFetch(prev => !prev); // Toggle the state to trigger useEffect
            }
        } catch (error) {
            console.error('Error creating feedback:', error);
        }
    }

    const handleCancel = () => {
        setActiveTab('info')
        setIsEditMode(false);
    }

    const handleUpdate = async () => {
        const courseData = {
            header: courseName,
            description,
            startDate,
            durationDays,
            categories: category
        };
        console.log(courseData);
        try {
            const response = await axios.put(`${process.env.REACT_APP_BASE_URL}/courses/${courseId}`, courseData, { withCredentials: true });
            if (response.status === 200) {
                const updatedCourse = await response.json();
                setCourse(updatedCourse);
                setIsEditMode(false);
                console.log("success");
            }
        } catch (error) {
            console.error('Error updating course:', error);
        }
    };

    const getToday = () => {
        return (new Date()).toISOString().slice(0, 10)
    }

    const getTwoYears = () => {
        const today = new Date();
        const twoYearsFromNow = new Date(today);
        twoYearsFromNow.setFullYear(today.getFullYear() + 2);
        const formattedTwoYearsFromNow = twoYearsFromNow.toISOString().slice(0, 10);
        return formattedTwoYearsFromNow;
    }

    return (
        <div className="course-detail-page">
            <div className="course-header">
                {isLoading ? <div>Loading...</div> :
                    <div className="course-info m-2">
                        {isEditMode ?
                            <input
                                type="text"
                                value={courseName}
                                onChange={(e) => setCourseName(e.target.value)}
                            />
                            : <h1>{course?.header}</h1>}
                        {!isEditMode &&
                            <Rating className={"mt-2 mb-2"}
                                    name="simple-controlled"
                                    value={course?.rating}
                                    readOnly
                                    precision={0.5}
                            />}
                        <p>Teacher: {teacherName}</p>
                        {
                            isEditMode ?
                                <div>
                                    <label htmlFor="start">Start date:</label>
                                    <input
                                        type="text"
                                        value={durationDays}
                                        onChange={(e) => setDurationDays(e.target.value)}
                                    />
                                </div>
                                : <p>Days: {course.durationDays ? (course?.durationDays) : "not set"}</p>}
                        {isEditMode ?
                            <div>
                                <label htmlFor="start">Start date:</label>
                                <input type="date" id="start" name="trip-start" value={startDate ? startDate : ''}
                                       min={getToday()}
                                       max={getTwoYears()}
                                       onChange={e => setStartDate((new Date(e.target.value)).toISOString().slice(0, 10))} />
                            </div>
                            :
                            <p>Start date: {course.startDate ? (course?.startDate.slice(0, 10)) : "not set"}</p>}
                        {isEditMode ?
                            <div>
                                <label htmlFor="my-select">Category:</label>
                                <select id="my-select" name="categories" onChange={e => setCategory(e.target.value)}>
                                    <option value="IT">IT</option>
                                    <option value="TECH">Tech</option>
                                    <option value="SCIENCE">Science</option>
                                    <option value="PSYCOLOGY">Psycology</option>
                                    <option value="OTHER">Other</option>
                                </select>
                            </div>
                            :
                            <p>Category: {course?.categories ? (course?.categories) : "not set"}</p>
                        }
                    </div>
                }
                {userRole === "STUDENT" &&

                    <button className="sign-up-button" disabled={userRole != "STUDENT"} onClick={
                    userEnrolled ? () => { handleDisenroll() } : () => { handleEnroll() }
                }>{userEnrolled ? "Disenroll" : "Enroll"}
                </button>}
                {userRole && userRole != "STUDENT" && isTeacherOwn && <button className="sign-up-button" onClick={() => {
                    setVerifyDelete(true)
                }}>Delete
                </button>
                }
                <DeleteCourseVerify
                    show={verifyDelete}
                    onHide={() => setVerifyDelete(false)}
                />
                {userRole && userRole != "STUDENT" && !isEditMode && isTeacherOwn &&
                    <div className="w-50 bg-warning logout text-center p-2 my-1"
                         style={{
                             "borderRadius": "30px",
                             "cursor": "pointer",
                             "margin": "0 0.5rem "
                         }}
                         onClick={handleEditButton}>
                        <span className={"btn btn-warning"} style={{ "padding": "0px" }}
                        >Edit
                        </span>
                    </div>
                }
                {userRole && userRole != "STUDENT" && isEditMode &&
                    <div className="bg-success w-50 logout text-center p-2 my-1"
                         style={{
                             "borderRadius": "30px",
                             "cursor": "pointer",
                             "margin": "0 0.5rem "
                         }}
                         onClick={handleUpdate}>
                        <span className={"btn btn-success"} style={{ "padding": "0px" }}
                        >Save
                        </span>
                    </div>
                }
                {userRole && userRole != "STUDENT" && isEditMode &&
                    <div className="bg-danger w-50 logout text-center p-2 my-1"
                         style={{
                             "borderRadius": "30px",
                             "cursor": "pointer",
                             "margin": "0 0.5rem "
                         }}
                         onClick={handleCancel}>
                        <span className={"btn btn-danger"} style={{ "padding": "0px" }}
                        >Cancel
                        </span>
                    </div>
                }
            </div>
            <div className="course-tabs">
                <button onClick={() => setActiveTab('info')} className={activeTab === 'info' ? 'active' : ''}>Info
                </button>
                {!isEditMode &&
                    <button onClick={() => {
                        setDescription("");
                        setActiveTab('comments')
                    }}
                            className={activeTab === 'comments' ? 'active' : ''}>Comments
                    </button>
                }
                <button onClick={() => setActiveTab('themes')}
                        className={activeTab === 'themes' ? 'active' : ''}>Themes
                </button>
                {
                    userRole && userRole === "TEACHER" && isTeacherOwn &&

                <button onClick={() => setTopicModalShow(true)}
                        className={activeTab === 'addtopic' ? 'active' : ''}>Add topic
                </button>
                }
            </div>
            <TopicCreateModal show={topicModalShow} courseId={courseId}
                              onHide={() => setTopicModalShow(false)}
                              onCreate={(newTopic) => {
                                  setTopics(prevTopics => [...prevTopics, newTopic]);
                              }}
            />
            <div className="course-content">
                {activeTab === 'info' && !isEditMode && <div>{(course?.description)}</div>}
                {activeTab === 'info' && isEditMode && <div>
                    <div>
                        <label>Description:</label>
                        <textarea
                            value={description}
                            onChange={(e) => setDescription(e.target.value)}
                            required
                        ></textarea>
                    </div>
                </div>}
                {(activeTab === 'comments' && !isEditMode) && <div className={"mb-3"}>
                    {userRole && userRole === "STUDENT" &&
                        <div className="d-flex flex-row">
                            {/*<div className="d-flex text-center justify-content-center align-items-center"><label>You may leave your course review:</label></div>*/}
                            <div className="mx-3 d-flex flex-column w-75 commsec">
                                <div>
                                    <Rating
                                        name="simple-controlled"
                                        value={commentRate}
                                        onChange={(event, newValue) => {
                                            setCommentRate(newValue)
                                        }}
                                    />
                                </div>
                                <div>
                                    <textarea
                                        className={"w-100"}
                                        value={comment}
                                        onChange={(e) => setComment(e.target.value)}
                                        required
                                    ></textarea>
                                </div>
                                <div className="d-flex justify-content-end w-100">
                                    <button className="btn btn-success mx-2"
                                            style={{
                                                "borderRadius": "30px",
                                                "cursor": "pointer",
                                                "margin": "0 0.5rem ",
                                                "width": "150px"
                                            }}
                                            onClick={handleCommentSave}>
                                        Post
                                    </button>
                                    <button className="btn btn-danger mx-2"
                                            style={{
                                                "borderRadius": "30px",
                                                "cursor": "pointer",
                                                "margin": "0 0.5rem ",
                                                "width": "150px"
                                            }}
                                            onClick={handleCommentCancel}>
                                        Cancel
                                    </button>
                                </div>
                            </div>
                        </div>}
                    <div className={"m-4 font-6 mb-2"}>{userRole === "STUDENT" ? "Other reviews:" : "Reviews:"}</div>
                    <div id="comment-container">
                        {course?.feedbacks.map((item, index) => (
                            <Comment key={index} name={item.fullname} text={item.text} rate={item.rating}/>
                        ))}
                    </div>
                </div>
                }
                {activeTab === 'themes' && <div>
                    <div id="accordion">
                        {topics.map((item, index) => (
                            <Collapse key={index} label={item.label} id={index} courseId={courseId}
                                      isTeacherOwn={isTeacherOwn} userEnrolled={userEnrolled} topicId={item.topicId} userRole={userRole}>
                                <div>
                                    {item.description}
                                </div>
                            </Collapse>
                        ))}
                    </div>
                </div>
                }
            </div>
        </div>
    );
};

export default Course;
