import React, { useState } from 'react';
import { Tabs, Tab, Form, Button } from 'react-bootstrap';
import axios from 'axios';

export default function TopicCreate({ courseId, onCreate, onHide }) {
    const [label, setLabel] = useState("");
    const [description, setDescription] = useState("");
    const [contents, setContents] = useState([{ type: "TEXT", title: "", body: "", file: null }]);
    const [key, setKey] = useState('details');
    const [createdTopic, setCreatedTopic] = useState(false);
    const [createdTopicId, setCreatedTopicId] = useState(0);

    const handleTopicSubmit = async (e) => {
        e.preventDefault();

        const payload = {
            courseId: 0,
            label,
            description,

        }

        const response = await axios.post(`/courses/${courseId}/topics/create`, payload,
            {
                withCredentials: 'include',
            });
        if(response.status!=201){
            console.log("cant create")
        }
        setCreatedTopicId(response.data.topicId)
        const newTopic = { label, description, courseId };

        onCreate(newTopic);
        // Close the modal
        onHide();
        // setIsCompleted(true);
        // Send payload to backend (e.g., axios.post('/api/topics', payload))
    }

    return (<>
        <Form onSubmit={handleTopicSubmit}>
            <Form.Group className="mb-3">
                <Form.Label>Label:</Form.Label>
                <Form.Control
                    value={label}
                    onChange={(e) => setLabel(e.target.value)}
                    placeholder="Enter topic label"
                />
            </Form.Group>
            <Form.Group className="mb-3">
                <Form.Label>Description:</Form.Label>
                <Form.Control
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    placeholder="Enter topic description"
                />
            </Form.Group>
            <div className="d-flex justify-content-end mx-4">
                <Button
                    variant="primary"
                    type="submit"
                    className="px-3"
                    style={{ backgroundColor: 'var(--main-blu)', color: 'var(--light)' }}
                >
                    Create
                </Button>
            </div>
        </Form>
    </>);
};

