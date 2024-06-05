import React from "react";
import '../../css/Comment.css'
function CourseComponent({ item }) {
    const printRate = (value) => {
        // const num = value.
        return parseFloat(value).toFixed(1);
    }
    return (
        <div className="card m-1 d-flex flex-column align-items-center h-100 productInstance">
            <div className="d-flex justify-content-center photo-cont">
                <img src="/logo512.png" className="card-img-top" alt="phone_photo"/>

            </div>
            <div className="card-body d-flex flex-column align-items-center">
                <h5 className="card-title card-model-title">{item?.header}</h5>
                <h6 className="card-title card-model-title">Rating: {printRate(item?.rating)} / 5</h6>
                <p className="card-text card-model-version">{item?.durationDays} days</p>
            </div>
        </div>
    );
}

export default CourseComponent;