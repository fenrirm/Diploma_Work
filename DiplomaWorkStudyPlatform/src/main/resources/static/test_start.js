document.addEventListener('DOMContentLoaded', function() {
    const contentElement = document.getElementById('test-content');
    const content = contentElement.getAttribute('data-content');
    const testId = document.getElementById('testId').value;

    const submitButton = document.getElementById('submitButton');

    const testContent = JSON.parse(content);
    let contentHtml = '';

    testContent.questions.forEach((question, index) => {
        let answerInputs = '';

        switch (question.questionType) {
            case 'singleChoice':
                answerInputs = question.answers.map((answer, ansIndex) => {
                    return `<li class="form-input">
                        <label>
                            <input type="radio" name="answer_${index}" value="${ansIndex}" data-question-id="${index}" data-answer-id="${ansIndex}">
                            ${answer}
                        </label>
                    </li>`;
                }).join('');
                break;
            case 'multipleChoice':
                answerInputs = question.answers.map((answer, ansIndex) => {
                    return `<li class="form-input">
                                <label>
                                    <input type="checkbox" name="answer_${index}" value="${ansIndex}" data-question-id="${index}" data-answer-id="${ansIndex}">
                                    ${answer}
                                </label>
                            </li>`;
                }).join('');
                break;
            case 'word':
                answerInputs = `<li class="form-input">
                                    <input type="text" name="answer_${index}" data-question-id="${index}">
                                </li>`;
                break;
        }

        contentHtml += `<div class="form-group question">
                            <label class="form-label">Question ${index + 1}: ${question.questionText}</label>
                            <ul>${answerInputs}</ul>
                        </div>`;
    });

    contentElement.innerHTML = contentHtml;

    const form = document.getElementById('testForm');

    document.getElementById('testForm').addEventListener('submit', function(event) {
        event.preventDefault();

        const answers = testContent.questions.map((question, index) => {
            let selectedAnswers = [];
            switch (question.questionType) {
                case 'singleChoice':
                    const singleChoiceAnswer = form.querySelector(`input[name="answer_${index}"]:checked`);
                    if (singleChoiceAnswer) {
                        selectedAnswers.push(parseInt(singleChoiceAnswer.value));
                    }
                    break;
                case 'multipleChoice':
                    const multipleChoiceAnswers = form.querySelectorAll(`input[name="answer_${index}"]:checked`);
                    multipleChoiceAnswers.forEach(answer => {
                        selectedAnswers.push(parseInt(answer.value));
                    });
                    break;
                case 'word':
                    const wordAnswer = form.querySelector(`input[name="answer_${index}"]`);
                    if (wordAnswer) {
                        selectedAnswers.push(wordAnswer.value);
                    }
                    break;
            }
            return {
                questionID: index,
                correctAnswers: selectedAnswers
            };
        });

        console.log(JSON.stringify(answers));
        console.log(testId);

        const submitData = {
            testId: testId,
            json: JSON.stringify(answers)

        };

        fetch('/submitTest', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(submitData)
        })
            .then(response => {
                if (response.ok) {
                    window.location.href = '/student_tests';
                } else {
                    console.error('Failed to submit test');
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });
    });

    // Handle navigation link clicks
    document.querySelectorAll('.nav-link').forEach(link => {
        link.addEventListener('click', function(event) {
            event.preventDefault();
            if (confirm("When switching to another tab, the test will be completed and the results will be saved.")) {
                submitButton.click();
            }
        });
    });
});
