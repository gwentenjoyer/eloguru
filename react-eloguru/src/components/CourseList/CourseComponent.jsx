import React from "react";
import '../../css/CourseComponent.css'
import { useNavigate } from "react-router-dom";
import Rating from "@mui/material/Rating";

function CourseComponent({ item }) {
    const printRate = (value) => {
        return parseFloat(value).toFixed(1);
    }

    // const handleClick = (event) => {
    //     const id = event.currentTarget.id;
    //     window.location.href = `${process.env.REACT_APP_BASE_URL}/courses/${id}`;
    //
    //     // console.log(`The div with id "${id}" was clicked.`);
    // };

    const navigate = useNavigate();

    const handleClick = (event) => {
        const id = event.currentTarget.id;
        navigate(`/course/${id}`);
    };

    return (
        <div className="card m-1 d-flex flex-column align-items-center h-100 productInstance" id={item.id} onClick={handleClick}>
            <div className="d-flex justify-content-center photo-cont">
                <img src={item?.photo ? `coursesPhotos/${item.photo}` : "emptyPhotoCourse.png"} className="card-img-top" alt="phone_photo"/>

            </div>
            <div className="card-body d-flex flex-column align-items-center">
                <h5 className="card-title card-model-title">{item?.header}</h5>
                <Rating
                    name="simple-controlled"
                    value={item?.rating}
                    readOnly
                    precision={0.5}
                />
                <p className="card-text card-model-version">{item?.durationDays} days</p>
            </div>
        </div>
    );

}

export default CourseComponent;
