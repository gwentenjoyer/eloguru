import React, { useState } from 'react';
import {useNavigate} from "react-router-dom";

function CoursePreview({ label, id }) {
    const navigate = useNavigate();
    const handleToggle = () => {
        navigate(`/course/${id}`);
    };
    return (
        <div className="card w-50 m-1">
            <div className="card-header prof-course-butе" id={`heading${id}`} onClick={handleToggle}>
                <h5 className="mb-0">
                        {label}
                </h5>
            </div>

        </div>
    );
}

export default CoursePreview;
