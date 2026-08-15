import React from 'react';
import './RulePage.css';

function RulePage() {
    return (
        <div className="rule-page">
            <div className="rules-container">
                <section><h1>Rules</h1></section>
                <section>
                    <h2>Objective</h2>
                    <p>Fill the grid to reveal a hidden picture using the given clues..</p>
                </section>
                <section>
                    <h2>Game grid</h2>
                    <p>Each cell can be filled (white) or empty (black).</p>
                </section>
                <section>
                    <h2>Clues</h2>
                    <p>Numbers indicate consecutive filled cells.</p>
                    <p>Multiple numbers mean separate groups, with at least one empty cell between them.</p>
                    <p>The order of numbers matches the group order.</p>
                </section>
            </div>
        </div>

    );
}

export default RulePage;

