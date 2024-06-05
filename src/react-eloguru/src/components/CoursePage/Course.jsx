import React, {useEffect, useState} from 'react';
import '../../css/CourseDetailPage.css';
import Collapse from "./Collapse";
import Comment from "./Comment";

const Course = ({courseId}) => {
    const [activeTab, setActiveTab] = useState('info');
    const [activeTopic, setActiveTopic] = useState('info');
    const [course, setCourse] = useState();
    const [isLoading, setIsLoading] = useState(true);

    // setCourse(test)
    useEffect(() => {
            const fetchUserInfo = async () => {
                console.log(courseId)
                const response = await fetch(`${process.env.REACT_APP_SERVER_URL}/courses/${courseId}`, {credentials: 'include'});
                const data = await response.json();
                console.log(data);
                // {
                //     "surname":"nick"
                //     ,"name":"sur"
                //     ,"id":0,
                //     "categories":["A1"],
                //     "age":0
                // }
                setCourse(data);
                // let test = {name: "sdf", description: "sdf", rating: 5, hours: "5"}
                // setCourse(test);
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


    return (
        <div className="course-detail-page">
            <div className="course-header">
                {isLoading ? <div>Loading...</div> :
                    <div className="course-info m-2">
                        <h1 >{(course?.header)}</h1>
                        <p>Rate: {printRate(course?.rating)} / 5</p>

                        <p>Days: {(course?.durationDays)}</p>
                    </div>
                }
                <button className="sign-up-button">Enroll</button>
            </div>
            <div className="course-tabs">
                <button onClick={() => setActiveTab('info')} className={activeTab === 'info' ? 'active' : ''}>Info
                </button>
                <button onClick={() => setActiveTab('comments')}
                        className={activeTab === 'comments' ? 'active' : ''}>Comments
                </button>
                <button onClick={() => setActiveTab('themes')}
                        className={activeTab === 'themes' ? 'active' : ''}>Themes
                </button>
            </div>
            <div className="course-content">
                {activeTab === 'info' && <div>{(course?.description)}

                </div>}
                {activeTab === 'comments' && <div>
                    <div id="comment-container">
                        {course?.feedbacks.map((item, index) => (
                            <Comment name={item.fullname} text={item.text}>
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
