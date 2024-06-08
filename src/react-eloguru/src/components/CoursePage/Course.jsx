import React, {useEffect, useState} from 'react';
import '../../css/CourseDetailPage.css';
import Collapse from "./Collapse";
import Comment from "./Comment";
import axios from "axios";

const Course = ({courseId}) => {
    const [activeTab, setActiveTab] = useState('info');
    const [activeTopic, setActiveTopic] = useState('info');
    const [course, setCourse] = useState();
    const [isLoading, setIsLoading] = useState(true);
    const [isEditMode, setIsEditMode] = useState(false);


    const [courseName, setCourseName] = useState('');
    const [description, setDescription] = useState('');
    const [durationDays, setDurationDays] = useState('');
    const [startDate, setStartDate] = useState('');
    const [category, setCategory] = useState('');

    // setCourse(test)
    useEffect(() => {
            const fetchUserInfo = async () => {
                console.log(courseId)
                const response = await fetch(`${process.env.REACT_APP_SERVER_URL}/courses/${courseId}`, {credentials: 'include'});
                if (!response.ok) {
                    throw new Error('Course not found');
                    //TODO: не перекидає на ерор пейдж
                }
                const data = await response.json();
                console.log("31", data);
                setCourse(data);
                setCourseName(data?.header)
                setDescription(data?.description)
                setDurationDays(data?.durationDays)
                setStartDate(data?.startDate? data?.startDate?.slice(0, 10): '')
                setCategory(data?.categories)
                setIsLoading(false);
            };

            fetchUserInfo();
        },
        []);

    // const showLoaded = (content) => {
    function showLoaded(content){
        console.log(isLoading)
        return isLoading || !course ? "Loading..." : content;
    }

    const fetchTopics = async () => {
        const response = await fetch(`${process.env.REACT_APP_SERVER_URL}/courses/4/topics`, {credentials: 'include'});
        console.log(response)
        return response;
    }

    const printRate = (value) => {
        // const num = value.
        return parseFloat(value).toFixed(1);
    }

    const getUserRole = () => {
        return JSON.parse(localStorage.getItem("account")).role;
    }

    const handleEnroll = async () => {
        // const response = await axios.post(`${process.env.REACT_APP_SERVER_URL}/courses/${courseId}/enroll`, {withCredentials: true});
        const response = await fetch(`${process.env.REACT_APP_SERVER_URL}/courses/${courseId}/enroll`, {credentials: 'include', method: "POST"});

        if (!response.status === 200){
            console.log(response)
            console.error("Failed to enroll")
        }
        console.log("enrolled")
        // navigate('/');
        // window.location.href = "/"

    };

    const handleEditButton = () => {
        setActiveTab('info')
        setIsEditMode(!isEditMode);
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

    // const handleUpdate = async () => {
    //     // const response = await fetch(`${process.env.REACT_APP_SERVER_URL}/courses/${courseId}/delete`, {credentials: 'include', method: "POST"});
    //     //
    //     // if (!response.status === 200){
    //     //     console.log(response)
    //     //     console.error("Failed to enroll")
    //     // }
    //     console.log("update")
    //     // navigate('/');
    //     // window.location.href = "/"
    //
    // };

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
                        {isEditMode?
                            <input
                                type="text"
                                value={ courseName}
                                onChange={(e) => setCourseName(e.target.value)}
                            />
                            : <h1>{(course?.header)}</h1>}
                        {!isEditMode && <p>Rate: {printRate(course?.rating)} / 5</p>}
                        {
                            isEditMode?
                                <div>

                                <label htmlFor="start">Start date:</label>

                            <input
                                type="text"
                                value={ durationDays}
                                onChange={(e) => setDurationDays(e.target.value)}
                            />

                                </div>
                            : <p>Days: {course.durationDays ? (course?.durationDays) : "not set"}</p>}
                        {isEditMode ?
                            <div>
                                <label htmlFor="start">Start date:</label>
                                {}
                                <input type="date" id="start" name="trip-start" value={startDate? startDate: ''} min={getToday()}
                                       max={getTwoYears()}
                                       onChange={e => setStartDate((new Date(e.target.value)).toISOString().slice(0,10))}/>
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
                <button className="sign-up-button" disabled={getUserRole() != "STUDENT"} onClick={() => {
                    handleEnroll()
                }}>Enroll
                </button>
                {getUserRole() != "STUDENT" && <button className="sign-up-button" onClick={() => {
                    handleDelete()
                }}>Delete
                </button>
                }

                {getUserRole() != "STUDENT" && !isEditMode &&
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

                {getUserRole() != "STUDENT" && isEditMode &&
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
                {getUserRole() != "STUDENT" && isEditMode &&
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
                    <button onClick={() => setActiveTab('comments')}
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
                {(activeTab === 'comments' && !isEditMode) && <div>
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
                        {course?.topics.map((item, index) => (
                            <Collapse key={index} label={item.label} id={index}>
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
