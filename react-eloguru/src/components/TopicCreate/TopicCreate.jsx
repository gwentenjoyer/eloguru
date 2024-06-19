// import React, { useState } from 'react';
// import {Link, useNavigate} from 'react-router-dom';
// import axios from 'axios';
// import Form from "react-bootstrap/Form";
// import Button from "react-bootstrap/Button";
//
// const TopicCreate = () => {
//     const[label, setLabel] = useState("");
//     const[description, setDescription] = useState("");
//
//     const handleSubmit =  (e) => {
//         e.preventDefault();
//         const payload = {
//             courseId:0,
//             label,
//             description
//         }
//         console.log(payload)
//     }
//
//     return (
//         <Form onSubmit={handleSubmit}>
//                 <Form.Group className="mb-3">
//                     <Form.Label>Label:</Form.Label>
//                     <Form.Control
//                         value={label}
//                         onChange={(e) => setLabel(e.target.value)}
//                         placeholder="Enter your email"/>
//                 </Form.Group>
//                 <Form.Group className="mb-3">
//                     <Form.Label>Description:</Form.Label>
//                     <Form.Control
//                         type="password"
//                         placeholder="Enter your password"
//                         // id={"lg-signup-pw"}
//                         value={description}
//                         onChange={(e) => setDescription(e.target.value)}
//                     />
//                 </Form.Group>
//
//                 <div className={"row justify-content-center my-3"}>
//                     <Button variant="primary" type="submit" className="justify-content-evenly lg-signup-submit">
//                         Sign up
//                     </Button>
//                 </div>
//         </Form>
//     );
// };
//
// export default TopicCreate;



import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";

const TopicCreate = () => {
    const [label, setLabel] = useState("");
    const [description, setDescription] = useState("");
    const [contents, setContents] = useState([{ title: "", body: "" }]);

    const handleSubmit = (e) => {
        e.preventDefault();
        const payload = {
            courseId: 0,
            label,
            description,
            contents
        }
        console.log(payload)
        // Send payload to backend (e.g., axios.post('/api/topics', payload))
    }

    const handleContentChange = (index, event) => {
        const newContents = contents.map((content, i) => {
            if (i === index) {
                return { ...content, [event.target.name]: event.target.value };
            }
            return content;
        });
        setContents(newContents);
    }

    const addContentField = () => {
        setContents([...contents, { title: "", body: "" }]);
    }

    return (
        <Form onSubmit={handleSubmit}>
            <Form.Group className="mb-3">
                <Form.Label>Label:</Form.Label>
                <Form.Control
                    value={label}
                    onChange={(e) => setLabel(e.target.value)}
                    placeholder="Enter topic label" />
            </Form.Group>
            <Form.Group className="mb-3">
                <Form.Label>Description:</Form.Label>
                <Form.Control
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    placeholder="Enter topic description" />
            </Form.Group>
            {contents.map((content, index) => (
                <div key={index}>
                    <Form.Group className="mb-3">
                        <Form.Label>Content Title:</Form.Label>
                        <Form.Control
                            name="title"
                            value={content.title}
                            onChange={(e) => handleContentChange(index, e)}
                            placeholder="Enter content title" />
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <Form.Label>Content Body:</Form.Label>
                        <Form.Control
                            name="body"
                            value={content.body}
                            onChange={(e) => handleContentChange(index, e)}
                            placeholder="Enter content body" />
                    </Form.Group>
                </div>
            ))}
            <Button variant="secondary" onClick={addContentField}>
                Add Content
            </Button>
            <div className="row justify-content-center my-3">
                <Button variant="primary" type="submit">
                    Create Topic
                </Button>
            </div>
        </Form>
    );
};

export default TopicCreate;
