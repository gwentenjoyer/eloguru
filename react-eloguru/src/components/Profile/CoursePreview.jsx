import React, { useState } from 'react';
import {useNavigate} from "react-router-dom";
import "../../css/CoursePreview.css"
function CoursePreview({ label, id, progress, isCompleted=false}) {
    const navigate = useNavigate();
    const handleToggle = () => {
        navigate(`/course/${id}`);
    };
    return (
        <div className="card w-50 m-1">
            <div
                className={`card-header prof-course-bute ${progress >= 100 ? "bg-success" : ""}`}
                id={`heading${id}`}
                onClick={handleToggle}
            >
                <div className="header-content">
                    <h5 className="mb-0">
                        {label}
                    </h5>
                    <h6 className="mb-0 progress-text">
                        {progress}%
                    </h6>
                </div>
            </div>
        </div>

    );
}

export default CoursePreview;
