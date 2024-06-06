import React, { useState } from 'react';
import {useNavigate} from "react-router-dom";

function CoursePreview({ label, id }) {
    const navigate = useNavigate();
    const handleToggle = () => {
        navigate(`/course/${id}`);
    };
    return (
        <div className="card">
            <div className="card-header" id={`heading${id}`}>
                <h5 className="mb-0">
                    <button
                        className="btn btn-link"
                        onClick={handleToggle}
                    >
                        {label}
                    </button>
                </h5>
            </div>

        </div>
    );
}

export default CoursePreview;
