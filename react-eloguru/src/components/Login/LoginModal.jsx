

import Modal from 'react-bootstrap/Modal';
import ControlledTabsExample from "./Tabs";
import "../../css/Login.css"

export default function LoginModal(props) {
    return (
        <Modal
            {...props}
            size="lg"
            aria-labelledby="contained-modal-title-vcenter"
            className="modal-log"
            centered
        >
            <Modal.Body>
                <ControlledTabsExample/>
            </Modal.Body>
            {/*<Modal.Footer className={"justify-content-evenly"}>*/}
            {/*    <Button onClick={props.onHide}>Close</Button>*/}
            {/*    <Button onClick={props.onHide}>Log in</Button>*/}
            {/*</Modal.Footer>*/}
        </Modal>
    );
}
