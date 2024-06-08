import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const CreateCourse = () => {
    const [courseName, setCourseName] = useState('');
    const [description, setDescription] = useState('');
    const [durationDays, setDurationDays] = useState('');
    const [startDate, setStartDate] = useState('');
    const [category, setCategory] = useState('');
    const [error, setError] = useState('');
    const [success, setSuccess] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();
        const courseData = {
            name: courseName,
            description,
            startDate,
            durationDays,
            category
        };
console.log(courseData);
        // try {
        //     const response = await axios.post(`${process.env.REACT_APP_SERVER_URL}/courses`, courseData, { withCredentials: true });
        //     if (response.status === 201) {
        //         setSuccess(true);
        //         setCourseName('');
        //         setDescription('');
        //         // Optionally navigate to another page or show a success message
        //         navigate('/profile');
        //     }
        // } catch (error) {
        //     console.error('Error creating course:', error);
        //     setError('Failed to create course. Please try again.');
        // }
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
        <div className="create-course">
            <h2>Create a New Course</h2>
            {success && <p className="success-message">Course created successfully!</p>}
            {error && <p className="error-message">{error}</p>}
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Course Name:</label>
                    <input
                        type="text"
                        value={courseName}
                        onChange={(e) => setCourseName(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="start">Start date:</label>
                    <input type="date" id="start" name="trip-start" value={getToday()} min={getToday()}
                           max={getTwoYears()} onChange={e => setStartDate((new Date(e.target.value)).toISOString())}/>
                </div>
                <div>
                    <label>Duration days:</label>
                    <input
                        type="text"
                        value={durationDays}
                        onChange={(e) => setDurationDays(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="my-select">Category:</label>
                    <select id="my-select" name="fruits" onChange={e => setCategory(e.target.value)}>
                        <option value="apple">Apple</option>
                        <option value="banana">Banana</option>
                        <option value="cherry">Cherry</option>
                    </select>
                </div>
                <div>
                    <label>Description:</label>
                    <textarea
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        required
                    ></textarea>
                </div>
                <button type="submit">Create Course</button>
            </form>
        </div>
    );
};

export default CreateCourse;
