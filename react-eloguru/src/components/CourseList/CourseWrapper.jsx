import React from 'react';
import { useParams } from 'react-router-dom';
import Course from '../CoursePage/Course';

const CourseWrapper = () => {
    const { id } = useParams();
    return <Course courseId={id} />;
};

export default CourseWrapper;