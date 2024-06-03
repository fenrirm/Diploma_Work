document.addEventListener("DOMContentLoaded", function () {
    let questionIndex = 0;

    function addQuestion(questionType) {
        questionIndex++;
        const questionsContainer = document.getElementById('questionsContainer');
        const questionDiv = document.createElement('div');
        questionDiv.className = 'question';
        questionDiv.setAttribute('data-question-type', questionType);

        let questionTypeLabel;
        switch (questionType) {
            case 'singleChoice':
                questionTypeLabel = 'Single Choice Question';
                break;
            case 'multipleChoice':
                questionTypeLabel = 'Multiple Choice Question';
                break;
            case 'word':
                questionTypeLabel = 'Word Question';
                break;
            default:
                questionTypeLabel = 'Question';
        }

        questionDiv.innerHTML = `
            <h4>${questionTypeLabel} ${questionIndex}</h4>
            <label for="questionText_${questionIndex}">Question Text:</label>
            <input type="text" id="questionText_${questionIndex}" name="questionText_${questionIndex}" class="question-text" required>

            <div class="answers" id="answers_${questionIndex}">
                <label>Answers:</label>
                <!-- Answers will be dynamically added here -->
            </div>

            <div class="difficulty-level">
                <label for="difficulty_${questionIndex}">Difficulty Level:</label>
                <select id="difficulty_${questionIndex}" name="difficulty_${questionIndex}">
                    <option value="1">Easy (1 point)</option>
                    <option value="2">Medium (2 points)</option>
                    <option value="3">Hard (3 points)</option>
                </select>
            </div>

            <button type="button" class="addAnswerBtn" data-question-index="${questionIndex}">Add Answer</button>
            <button type="button" class="removeQuestionBtn" data-question-index="${questionIndex}">Remove Question</button>
        `;

        questionsContainer.appendChild(questionDiv);

        // Add event listener to the "Add Answer" button
        questionDiv.querySelector(`.addAnswerBtn[data-question-index="${questionIndex}"]`).addEventListener('click', function () {
            const questionIndex = this.getAttribute('data-question-index');
            addAnswer(questionIndex, questionType);
        });

        // Add event listener to the "Remove Question" button
        questionDiv.querySelector(`.removeQuestionBtn[data-question-index="${questionIndex}"]`).addEventListener('click', function () {
            questionsContainer.removeChild(questionDiv);
            reindexQuestions();
        });
    }

    function addAnswer(questionIndex, questionType) {
        const answersContainer = document.getElementById(`answers_${questionIndex}`);

        // Check if there's already an answer for Word Question
        if (questionType === 'word' && answersContainer.querySelector('input[type="text"]')) {
            alert('Word Question can only have one answer.');
            return;
        }

        const answerDiv = document.createElement('div');

        const answerInput = document.createElement('input');
        answerInput.type = 'text';
        answerInput.name = `answer_${questionIndex}`;
        answerInput.required = true;

        let answerElement;
        if (questionType === 'word') {
            answerElement = document.createElement('input');
            answerElement.type = 'radio';
            answerElement.name = `correctAnswer_${questionIndex}`;
            answerElement.checked = true; // Automatically mark the first answer as correct for Word Question
        } else if (questionType === 'singleChoice') {
            answerElement = document.createElement('input');
            answerElement.type = 'radio';
            answerElement.name = `correctAnswer_${questionIndex}`;
        } else if (questionType === 'multipleChoice') {
            answerElement = document.createElement('input');
            answerElement.type = 'checkbox';
            answerElement.name = `correctAnswer_${questionIndex}[]`;
        }

        const label = document.createElement('label');
        label.innerHTML = 'Correct Answer';

        const removeAnswerBtn = document.createElement('button');
        removeAnswerBtn.type = 'button';
        removeAnswerBtn.innerHTML = 'Remove Answer';
        removeAnswerBtn.addEventListener('click', function () {
            answersContainer.removeChild(answerDiv);
        });

        answerDiv.appendChild(answerInput);
        answerDiv.appendChild(answerElement);
        answerDiv.appendChild(label);
        answerDiv.appendChild(removeAnswerBtn);

        answersContainer.appendChild(answerDiv);
    }


    function reindexQuestions() {
        const questions = document.querySelectorAll('.question');
        questionIndex = 0;
        questions.forEach((question) => {
            questionIndex++;
            const questionIndexLabel = question.querySelector('h4');
            questionIndexLabel.textContent = questionIndexLabel.textContent.replace(/\d+/, questionIndex);
            question.querySelectorAll('button[data-question-index]').forEach((button) => {
                button.setAttribute('data-question-index', questionIndex);
            });
            question.querySelectorAll('input[id^="questionText_"]').forEach((input) => {
                input.id = input.id.replace(/\d+/, questionIndex);
                input.name = input.name.replace(/\d+/, questionIndex);
            });
            question.querySelectorAll('div[id^="answers_"]').forEach((div) => {
                div.id = div.id.replace(/\d+/, questionIndex);
            });
        });
    }

    document.getElementById('addSingleChoiceQuestion').addEventListener('click', function () {
        addQuestion('singleChoice');
    });

    document.getElementById('addMultipleChoiceQuestion').addEventListener('click', function () {
        addQuestion('multipleChoice');
    });

    document.getElementById('addWordQuestion').addEventListener('click', function () {
        addQuestion('word');
    });


    document.getElementById('createTestForm').addEventListener('submit', function (event) {
        event.preventDefault();

        const testName = document.getElementById('testName').value;
        const totalPoints = document.getElementById('totalPoints').value;
        const courseId = document.getElementById('courseId').value;

        let questionIndex = 0;
        const questions = [];
        const questionElements = document.querySelectorAll('.question');
        questionElements.forEach((questionElement) => {
            const questionText = questionElement.querySelector('.question-text').value;
            const questionType = questionElement.getAttribute('data-question-type');

            const answers = [];
            const answerElements = questionElement.querySelectorAll('input[name^="answer_"]');
            answerElements.forEach((answerElement) => {
                answers.push(answerElement.value);
            });
            const correctAnswers = [];
            const correctAnswerElements = questionElement.querySelectorAll('input[name^="correctAnswer_"]');
            correctAnswerElements.forEach((correctAnswerElement, index) => {
                if (correctAnswerElement.checked) {
                    correctAnswers.push(index);
                }
            });
            const difficultyLevelSelect = questionElement.querySelector('select');
            const difficultyLevel = difficultyLevelSelect.value;
            const questionData = {
                questionID:  questionIndex,
                questionText: questionText,
                questionType: questionType,
                answers: answers,
                correctAnswers: correctAnswers,
                difficultyLevel: difficultyLevel
            };
            questions.push(questionData);
            ++questionIndex;
        });

        const testData = {
            json: JSON.stringify({
                testName: testName,
                totalPoints: totalPoints,
                questions: questions
            }),
            courseId: courseId,
            testName: testName,
            totalPoints: totalPoints
        };

        fetch('/saveTest', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(testData)
        })
            .then(response => {
                if (response.ok) {
                    window.location.href = '/teacher_edit_course/'+ courseId;
                } else {
                    console.error('Failed to save test');
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });
    });

});
