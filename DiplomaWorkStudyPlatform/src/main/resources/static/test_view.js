document.addEventListener('DOMContentLoaded', function() {
    const contentElement = document.getElementById('test-content');
    const content = contentElement.getAttribute('data-content');

    try {
        const testContent = JSON.parse(content);
        let contentHtml = `<div class="form-group">
                                <label class="form-label">Test Name:</label>
                                <p class="form-input">${testContent.testName}</p>
                           </div>`;
        contentHtml += `<div class="form-group">
                                <label class="form-label">Total Points:</label>
                                <p class="form-input">${testContent.totalPoints}</p>
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
                                        <input type="radio" name="question_${index}" disabled ${isChecked}>
                                        ${answer}
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
                                        <input type="checkbox" name="question_${index}" disabled ${isChecked}>
                                        ${answer}
                                    </label>
                                </li>`;
                    }).join('');
                    break;
                case 'word':
                    questionTypeLabel = 'Word';
                    answerInputs = question.answers.map((answer, ansIndex) => {
                        return `<li class="form-input">Answer ${ansIndex + 1}: ${answer}</li>`;
                    }).join('');
                    break;
                default:
                    questionTypeLabel = 'Unknown Type';
            }

            contentHtml += `<div class="form-group question">
                    <label class="form-label">Question ${index + 1}:</label>
                    <p class="form-input">${question.questionText}</p>
                    <label class="form-label">Question Type:</label>
                    <p class="form-input">${questionTypeLabel}</p>
                    <label class="form-label">Answers:</label>
                    <ul>${answerInputs}</ul>
                    <label class="form-label">Difficulty Level:</label>
                    <p class="form-input">${question.difficultyLevel}</p>
                </div>`;
        });

        contentElement.innerHTML = contentHtml;
    } catch (e) {
        console.error('Error parsing test content JSON:', e);
        contentElement.innerHTML = '<p>Error loading test content.</p>';
    }
});
