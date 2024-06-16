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
    const [selectedFile, setSelectedFile] = useState(null);

    const handleFileChange = (event) => {
        setSelectedFile(event.target.files[0]);
    };


    const handleSubmit = async (event) => {
        event.preventDefault();

        // const courseData = new FormData({
        //     header: courseName,
        //     description,
        //     startDate,
        //     durationDays,
        //     categories: category
        // });

        const courseData = new FormData();
        courseData.append('header', courseName);
        courseData.append('description', description);
        courseData.append('startDate', startDate);
        courseData.append('durationDays', durationDays);
        courseData.append('categories', category);

        // const formData = new FormData();
        // formData.append(...courseData);
        // formData.
        courseData.append('photo', selectedFile);

        // console.log(courseData.values);
        for (const pair of courseData.entries()) {
            console.log(pair[0] + ': ' + pair[1]);
        }
        axios.post(`${process.env.REACT_APP_BASE_URL}/courses/create`, courseData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
            withCredentials: true
        })
            .then((response) => {
                console.log(response.data);
                if (response.status === 201) {
                    setSuccess(true);
                    setCourseName('');
                    setDescription('');
                    setCategory('');
                    setStartDate('');
                    setDurationDays('');
                    // Optionally navigate to another page or show a success message
                    navigate('/course');
                }
            })
            .catch((error) => {
                console.error('Error creating course:', error);
                setError('Failed to create course. Please try again.');
            });

    //     try {
    //         const response = await axios.post(`${process.env.REACT_APP_BASE_URL}/courses/create`, courseData, { withCredentials: true });
    //         if (response.status === 201) {
    //             setSuccess(true);
    //             setCourseName('');
    //             setDescription('');
    //             setCategory('');
    //             setStartDate('');
    //             setDurationDays('');
    //             // Optionally navigate to another page or show a success message
    //             navigate('/courses');
    //         }
    //     } catch (error) {
    //         console.error('Error creating course:', error);
    //         setError('Failed to create course. Please try again.');
    //     }
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
                        type="number"
                        value={durationDays}
                        onChange={(e) => setDurationDays(e.target.value)}
                        required
                    />
                </div>
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
                <div>
                    <label>Description:</label>
                    <textarea
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        required
                    ></textarea>
                </div>
                <div>
                    <label>Course photo:</label>
                    <input
                        type="file"
                        onChange={handleFileChange}
                    />
                </div>
                <button type="submit">Create course</button>
            </form>
        </div>
    );
};

export default CreateCourse;
