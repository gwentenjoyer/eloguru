

import Modal from 'react-bootstrap/Modal';
import "../../css/Login.css"
import TopicCreate from "./TopicCreate";

export default function TopicCreateModal(props) {
    return (
        <Modal
            {...props}
            size="lg"
            aria-labelledby="contained-modal-title-vcenter"
            className="modal-log"
            centered
        >
            <Modal.Body>
                <TopicCreate
                    courseId={props.courseId}
                    onCreate={(newTopic) => {
                        props.onCreate(newTopic);
                    }}
                    onHide={props.onHide}
                />
            </Modal.Body>
            {/*<Modal.Footer className={"justify-content-evenly"}>*/}
            {/*    <Button onClick={props.onHide}>Close</Button>*/}
            {/*    <Button onClick={props.onHide}>Log in</Button>*/}
            {/*</Modal.Footer>*/}
        </Modal>
    );
}
