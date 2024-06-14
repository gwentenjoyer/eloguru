import React, {useEffect, useState} from 'react';
import '../../css/CourseDetailPage.css';
import Collapse from "./Collapse";
import Comment from "./Comment";
import axios from "axios";
import Rating from '@mui/material/Rating';

const Course = ({courseId}) => {

    const localcourses = localStorage.getItem("studentEnrolled");
    const [activeTab, setActiveTab] = useState('info');
    const [topics, setTopics] = useState('info');
    const [course, setCourse] = useState();
    const [isLoading, setIsLoading] = useState(true);
    const [isEditMode, setIsEditMode] = useState(false);
    const [userRole, setUserRole] = useState('');


    const [courseName, setCourseName] = useState('');
    const [description, setDescription] = useState('');
    const [durationDays, setDurationDays] = useState('');
    const [startDate, setStartDate] = useState('');
    const [category, setCategory] = useState('');
    const [rating, setRating] = useState('');
    const [comment, setComment] = useState('');
    const [commentRate, setCommentRate] = useState('');
    const [userEnrolled, setUserEnrolled] = useState(localcourses ? JSON.parse(localcourses).includes(courseId): false);


    useEffect(() => {
            const fetchUserInfo = async () => {
                try {
                    const [courseResponse, topicsResponse] = await Promise.all([
                        fetch(`${process.env.REACT_APP_SERVER_URL}/courses/${courseId}`, { credentials: 'include' }),
                        fetchTopics()
                    ]);

                    if (!courseResponse.ok) {
                        throw new Error(`Failed to fetch course data: ${courseResponse.statusText}`);
                    }

                    //TODO: не перекидає на ерор пейдж
                    const data = await courseResponse.json();
                    setCourse(data);
                    setTopics(await topicsResponse.json())
                    setCourseName(data?.header)
                    setDescription(data?.description)
                    setDurationDays(data?.durationDays)
                    setStartDate(data?.startDate? data?.startDate?.slice(0, 10): '')
                    setCategory(data?.categories)
                    await getUserRole();
                    setIsLoading(false);

                } catch (error) {
                    console.error("Error fetching course and topics:", error);
                    throw error;
                }
            };

            fetchUserInfo();
        },
        []);

    const fetchTopics = async () => {
        const response = await fetch(`${process.env.REACT_APP_SERVER_URL}/courses/${courseId}/topics`, {credentials: 'include'});
        console.log(response)
        return response;
    }

    const getUserRole = async () => {
        // return JSON.parse(localStorage.getItem("account")).role;
        const response = await fetch(`${process.env.REACT_APP_SERVER_URL}/accounts/check`, {credentials: 'include'});

        if (response.status == 200){
            setUserRole((await response.json()).role.toString().toUpperCase());
        }
    }

    const handleEnroll = async () => {
        // const response = await axios.post(`${process.env.REACT_APP_SERVER_URL}/courses/${courseId}/enroll`, {withCredentials: true});
        const response = await fetch(`${process.env.REACT_APP_SERVER_URL}/courses/${courseId}/enroll`, {credentials: 'include', method: "POST"});

        if (!response.status === 200){
            console.log(response)
            console.error("Failed to enroll")
        }
        console.log("enrolled")
        setUserEnrolled(true);

    };

    const handleDisenroll = async () => {
        const response = await fetch(`${process.env.REACT_APP_SERVER_URL}/courses/${courseId}/disenroll`, {credentials: 'include', method: "POST"});
        console.log("another", response)
        if (response.status !== 200){
            console.log(response)
            console.error("Failed to disenroll")
        }
        if (response.status === 200){
            console.log("disenrolled")
            setUserEnrolled(false);
        }
    };

    const handleEditButton = () => {
        setActiveTab('info')
        setIsEditMode(!isEditMode);
    }

    const handleCommentCancel = () => {

    }


    const handleCommentSave = async () => {
        const payload = {
            courseId,
            text: comment,
            rating: commentRate
        }
        try {
            const response = await fetch(`${process.env.REACT_APP_SERVER_URL}/feedbacks`, {
                credentials: 'include',
                method: "POST",
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(payload)
            });
            if (response.status === 201) {
                setComment("");
                setCommentRate(null);
                console.log("successfully created comment")
                // window.location.href=`/course/${courseId}`
            }
        } catch (error) {
            console.error('Error creating feedback:', error);
        }
    }


    const handleCancel = () => {
        setActiveTab('info')
        setIsEditMode();
    }

    const handleDelete = async () => {
        const response = await fetch(`${process.env.REACT_APP_SERVER_URL}/courses/${courseId}/delete`, {credentials: 'include', method: "POST"});

        if (!response.status === 200){
            console.log(response)
            console.error("Failed to enroll")
        }
        console.log("deleted")
        // navigate('/');
        // window.location.href = "/"

    };

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
            const response = await axios.put(`${process.env.REACT_APP_SERVER_URL}/courses/${courseId}`, courseData, { withCredentials: true });
            if (response.status === 200) {
                // setSuccess(true);
                // setCourseName('');
                // setDescription('');
                // setCategory('');
                // setStartDate('');
                // setDurationDays('');
                // Optionally navigate to another page or show a success message
                // navigate('/courses');
                console.log("success")
                window.location.href=`/course/${courseId}`
            }
        } catch (error) {
            console.error('Error creating course:', error);
            // setError('Failed to create course. Please try again.');
        }
    };

    const getToday = () => {
        return (new Date()).toISOString()
            .slice(0, 10)
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
                            : <h1>{(course?.header)}</h1>}
                        {!isEditMode &&
                            <Rating className={"mt-2 mb-2"}
                                    name="simple-controlled"
                                    value={course?.rating}
                                    readOnly
                                    precision={0.5}
                            />}
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
                                {}
                                <input type="date" id="start" name="trip-start" value={startDate ? startDate : ''}
                                       min={getToday()}
                                       max={getTwoYears()}
                                       onChange={e => setStartDate((new Date(e.target.value)).toISOString().slice(0, 10))}/>
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
                <button className="sign-up-button" disabled={userRole != "STUDENT"} onClick={
                    userEnrolled ? () => {handleDisenroll()}:  () => {handleEnroll()}
                    }>{userEnrolled ? "Disenroll" : "Enroll"}
                </button>
                {userRole && userRole != "STUDENT" && <button className="sign-up-button" onClick={() => {
                    handleDelete()
                }}>Delete
                </button>
                }

                {userRole && userRole != "STUDENT" && !isEditMode &&
                    <div className="w-50 bg-warning logout text-center p-2 my-1"
                         style={{
                             "borderRadius": "30px",
                             "cursor": "pointer",
                             "margin": "0 0.5rem "
                         }}
                         onClick={handleEditButton}>
                        <span className={"btn btn-warning"} style={{"padding": "0px"}}
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
                        <span className={"btn btn-success"} style={{"padding": "0px"}}
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
                        <span className={"btn btn-danger"} style={{"padding": "0px"}}
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
                        setActiveTab('comments')}}
                            className={activeTab === 'comments' ? 'active' : ''}>Comments
                    </button>
                }
                <button onClick={() => setActiveTab('themes')}
                        className={activeTab === 'themes' ? 'active' : ''}>Themes
                </button>
            </div>
            <div className="course-content">
                {activeTab === 'info' && !isEditMode && <div>{(course?.description)}

                </div>}
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
                        <div className="d-flex text-center justify-content-center align-items-center"><label>You may leave your course review:</label></div>
                        <div className="mx-3 d-flex flex-column w-75">
                            <div>

                                <Rating
                                    name="simple-controlled"
                                    value={commentRate}
                                    onChange={(event, newValue) => {setCommentRate(newValue)}}

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
                            <div className="d-flex flex-row justify-content-end">
                                <div className="bg-success w-50 logout text-center p-2 my-1"
                                     style={{
                                         "borderRadius": "30px",
                                         "cursor": "pointer",
                                         "margin": "0 0.5rem "
                                     }}

                                     onClick={handleCommentSave}>
                                                    <span className={"btn btn-success"} style={{"padding": "0px"}}
                                                    >Save
                                                    </span>
                                </div>
                                    <div className="bg-danger w-50 logout text-center p-2 my-1"
                                         style={{
                                             "borderRadius": "30px",
                                             "cursor": "pointer",
                                             "margin": "0 0.5rem "
                                         }}

                                         onClick={handleCommentCancel}>
                                                    <span className={"btn btn-danger"} style={{"padding": "0px"}}
                                                    >Cancel
                                                    </span>
                                    </div>
                            </div>
                        </div>

                    </div>}
                    <div></div>
                    <div id="comment-container">
                        {course?.feedbacks.map((item, index) => (
                            <Comment name={item.fullname} text={item.text} rate={item.rating}>
                            </Comment>
                        ))}

                    </div>
                </div>
                }
                {activeTab === 'themes' && <div>
                    <div id="accordion">
                        {console.log(topics)}
                        {topics.map((item, index) => (
                            <Collapse key={index} label={item.label} id={index} courseId={courseId} topicId={item.topicId} userRole={userRole}>
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
