import React from "react";
// import "bootstrap/dist/css/bootstrap.min.css"
// import "animate.css"

export default function AboutPage() {
    return (
        <div className={"container-xxl py-3"}>
            <div className={"container"}>
                <div className={"d-flex flex-column g-5 m-4"}>
                    <h2 className={"text-center about-text mb-4"}>About Eloguru</h2>
                    <p className={""}>
                        Welcome to Eloguru, your premier online platform for comprehensive and professional learning.
                        With a passion for accessible and effective learning, we strive to provide top-notch courses for
                        learners of all ages and experience levels. At Eloguru, we understand that learning is an
                        important milestone in everyone's life. That's why we create a supportive and encouraging
                        learning environment where our students can gain the knowledge, skills, and confidence they
                        need to achieve their goals.
                    </p>
                    <h4 className={"text-center m-4"} style={{"color":"#ffc107"}}>Why should you choose us?</h4>
                    <ul  className={"d-flex flex-column m-2 about-text "}>

                        <li style={{"color":"black", "marginBottom":"1%"}}>
                            Flexibility and convenience
                            We understand that life can be busy, so we offer flexible scheduling options to meet the
                            needs of our students. Whether you're a student, working professional or senior citizen,
                            we have courses and class schedules to suit your schedule.
                        </li>
                        <li style={{"color":"black", "marginBottom":"1%"}}>
                            Comprehensive curriculum:
                            We offer a comprehensive curriculum designed to cover all aspects of your field of study.
                            From theoretical classes to practical assignments, we equip our students with the necessary
                            skills to confidently apply their knowledge in real life
                        </li>
                        <li style={{"color":"black", "marginBottom":"1%"}}>
                            Experienced and certified teachers
                            Our team of highly qualified and certified teachers bring a wealth of knowledge and
                            experience to every course. Not only are they experts in their fields, but they also have
                            exceptional teaching abilities, ensuring that each student receives individualized
                            attention and guidance.
                        </li>
                        <li style={{"color":"black", "marginBottom":"1%"}}>
                            Join our community of learners and teachers, develop your skills and knowledge, and discover
                            new opportunities for personal and professional growth. Our platform is designed for you and
                            your success. Learn, teach, and grow with us on Eloguru!
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    );
}
