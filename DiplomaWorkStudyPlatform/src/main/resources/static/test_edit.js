document.addEventListener('DOMContentLoaded', function() {
    const contentElement = document.getElementById('test-content');
    const content = contentElement.getAttribute('data-content');

    try {
        const testContent = JSON.parse(content);
        let contentHtml = `<div class="form-group">
                                <label class="form-label">Test Name:</label>
                                <input type="text" id="testName" class="form-input" value="${testContent.testName}">
                           </div>`;
        contentHtml += `<div class="form-group">
                                <label class="form-label">Total Points:</label>
                                <input type="number" id="totalPoints" class="form-input" value="${testContent.totalPoints}">
                        </div>`;
        contentHtml += '<h3>Questions:</h3>';

        testContent.questions.forEach((question, index) => {
            let questionTypeLabel;
            let answerInputs = '';

            switch (question.questionType) {
                case 'singleChoice':
                    questionTypeLabel = 'Single Choice';
                    answerInputs = question.answers.map((answer, ansIndex) => {
                        const isChecked = question.correctAnswers.includes(ansIndex) ? 'checked' : '';
                        return `<li class="form-input">
                                    <label>
                                        <input type="radio" name="correctAnswer_${index}" value="${ansIndex}" ${isChecked}>
                                        <input type="text" name="answer_${index}" value="${answer}">
                                    </label>
                                </li>`;
                    }).join('');
                    break;
                case 'multipleChoice':
                    questionTypeLabel = 'Multiple Choice';
                    answerInputs = question.answers.map((answer, ansIndex) => {
                        const isChecked = question.correctAnswers.includes(ansIndex) ? 'checked' : '';
                        return `<li class="form-input">
                                    <label>
                                        <input type="checkbox" name="correctAnswer_${index}" value="${ansIndex}" ${isChecked}>
                                        <input type="text" name="answer_${index}" value="${answer}">
                                    </label>
                                </li>`;
                    }).join('');
                    break;
                case 'word':
                    questionTypeLabel = 'Word';
                    answerInputs = question.answers.map((answer, ansIndex) => {
                        return `<li class="form-input">
                                    Answer ${ansIndex + 1}: <input type="text" name="answer_${index}" value="${answer}">
                                </li>`;
                    }).join('');
                    break;
                default:
                    questionTypeLabel = 'Unknown Type';
            }

            contentHtml += `<div class="form-group question" data-question-type="${question.questionType}">
                    <label class="form-label">Question ${index + 1}:</label>
                    <input type="text" class="form-input question-text" value="${question.questionText}">
                    <label class="form-label">Question Type:</label>
                    <p class="form-input">${questionTypeLabel}</p>
                    <label class="form-label">Answers:</label>
                    <ul>${answerInputs}</ul>
                    <label class="form-label">Difficulty Level:</label>
                    <select class="form-input" name="difficulty_${index}">
                        <option value="1" ${question.difficultyLevel === 1 ? 'selected' : ''}>Easy (1 point)</option>
                        <option value="2" ${question.difficultyLevel === 2 ? 'selected' : ''}>Medium (2 points)</option>
                        <option value="3" ${question.difficultyLevel === 3 ? 'selected' : ''}>Hard (3 points)</option>
                    </select>
                </div>`;
        });

        contentElement.innerHTML = contentHtml;
    } catch (e) {
        console.error('Error parsing test content JSON:', e);
        contentElement.innerHTML = '<p>Error loading test content.</p>';
    }

    document.getElementById('editTestForm').addEventListener('submit', function(event) {
        event.preventDefault();

        const testId = document.getElementById('testId').value;  // Додаємо ID тесту
        const testName = document.getElementById('testName').value;
        const totalPoints = document.getElementById('totalPoints').value;

        const questions = [];
        const questionElements = document.querySelectorAll('.question');
        questionElements.forEach((questionElement, index) => {
            const questionText = questionElement.querySelector('.question-text').value;
            const questionType = questionElement.getAttribute('data-question-type');

            const answers = [];
            const answerElements = questionElement.querySelectorAll(`input[name="answer_${index}"]`);
            answerElements.forEach((answerElement) => {
                answers.push(answerElement.value);
            });

            const correctAnswers = [];
            if (questionType === 'singleChoice') {
                const correctAnswerElement = questionElement.querySelector(`input[name="correctAnswer_${index}"]:checked`);
                if (correctAnswerElement) {
                    correctAnswers.push(parseInt(correctAnswerElement.value));
                }
            } else if (questionType === 'multipleChoice') {
                const correctAnswerElements = questionElement.querySelectorAll(`input[name="correctAnswer_${index}"]:checked`);
                correctAnswerElements.forEach((correctAnswerElement) => {
                    correctAnswers.push(parseInt(correctAnswerElement.value));
                });
            }

            const difficultyLevelSelect = questionElement.querySelector(`select[name="difficulty_${index}"]`);
            const difficultyLevel = difficultyLevelSelect.value;

            const questionData = {
                questionText: questionText,
                questionType: questionType,
                answers: answers,
                correctAnswers: correctAnswers,
                difficultyLevel: difficultyLevel
            };

            questions.push(questionData);
        });

        const testData = {
            json: JSON.stringify({
                testName: testName,
                totalPoints: totalPoints,
                questions: questions
            }),
            testId: testId,
            testName: testName,
            totalPoints: totalPoints

        };

        fetch('/updateTest', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(testData)
        })
            .then(response => {
                if (response.ok) {
                    window.location.href = '/teacher_tests';
                } else {
                    console.error('Failed to update test');
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });
    });

});
