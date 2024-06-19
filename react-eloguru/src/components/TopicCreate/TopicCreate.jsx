import React, { useState } from 'react';
import { Tabs, Tab, Form, Button } from 'react-bootstrap';
import axios from 'axios';

const TopicCreate = ({courseId}) => {
    const [label, setLabel] = useState("");
    const [description, setDescription] = useState("");
    const [contents, setContents] = useState([{ type: "TEXT", title: "", body: "", file: null }]);
    const [key, setKey] = useState('details');
    const [createdTopic, setCreatedTopic] = useState(false);
    const [createdTopicId, setCreatedTopicId] = useState(0);

    const handleTopicSubmit = async (e) => {
        e.preventDefault();
        console.log("cour", courseId);

        const payload = {
            courseId: 0,
            label,
            description,

        }
        console.log(payload);

        const response = await axios.post(`${process.env.REACT_APP_BASE_URL}/courses/${courseId}/topics/create`, payload,
            {
                withCredentials: 'include',
            });
        if(response.status!=201){
            console.log("cant create")
        }
        setCreatedTopicId(response.data.topicId)
        console.log("crto", createdTopicId)
        // response.data
        console.log(response)
        // const data = await response
        console.log(response.status)
        // setIsCompleted(true);
        // Send payload to backend (e.g., axios.post('/api/topics', payload))
    }

const handleContentCreate = async () => {

}

    const handleContentChange = (index, event) => {
        const { name, value } = event.target;
        const newContents = contents.map((content, i) => {
            if (i === index) {
                return { ...content, [name]: value };
            }
            return content;
        });
        setContents(newContents);
    }

    const handleFileChange = (index, event) => {
        const file = event.target.files[0];
        const newContents = contents.map((content, i) => {
            if (i === index) {
                return { ...content, file };
            }
            return content;
        });
        setContents(newContents);
    }

    const addContentField = () => {
        setContents([...contents, { type: "TEXT", title: "", body: "", file: null }]);
    }

    return (
        <Tabs id="topic-create-tabs" activeKey={key}  onSelect={(k) => setKey(k)} className="mb-3">
            <Tab eventKey="details" title="Topic Details" disabled={createdTopic}>
                <Form onSubmit={handleTopicSubmit}>
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
                    <Button variant="primary" type="submit"
                            onClick={() => setKey('contents')}
                    >
                        Next: Add Contents
                    </Button>
                </Form>
            </Tab>
            <Tab eventKey="contents" title="Contents">
                <Form onSubmit={handleContentCreate}>
                    {contents.map((content, index) => (
                        <div key={index} className="mb-4">
                            <Form.Group className="mb-3">
                                <Form.Label>Content Type:</Form.Label>
                                <Form.Control
                                    as="select"
                                    name="type"
                                    value={content.type}
                                    onChange={(e) => handleContentChange(index, e)}>
                                    <option value="TEXT">Text</option>
                                    <option value="FILE">File</option>
                                </Form.Control>
                            </Form.Group>
                            <Form.Group className="mb-3">
                                <Form.Label>Content Title:</Form.Label>
                                <Form.Control
                                    name="title"
                                    value={content.title}
                                    onChange={(e) => handleContentChange(index, e)}
                                    placeholder="Enter content title" />
                            </Form.Group>
                            {content.type === "TEXT" ? (
                                <Form.Group className="mb-3">
                                    <Form.Label>Content Body:</Form.Label>
                                    <Form.Control
                                        name="body"
                                        value={content.body}
                                        onChange={(e) => handleContentChange(index, e)}
                                        placeholder="Enter content body" />
                                </Form.Group>
                            ) : (
                                <Form.Group className="mb-3">
                                    <Form.Label>Upload File:</Form.Label>
                                    <Form.Control
                                        type="file"
                                        onChange={(e) => handleFileChange(index, e)} />
                                </Form.Group>
                            )}
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
            </Tab>
        </Tabs>
    );
};

export default TopicCreate;
