import React from 'react';
import './RulePage.css';

function Cell({state}) {
    return <span className={'rule-cell rule-cell-'+state} aria-hidden="true">{state==='empty'?'x':''}</span>;
}
function Example({clue,states,description}) {
    return <div className="rule-example" role="img" aria-label={'Clue '+clue+'. '+description}>
        <span className="rule-clue">{clue.split(' ').map((number,i)=><span className="rule-clue-number" key={i}>{number}</span>)}</span>
        <div className="rule-example-cells">{states.map((state,i)=><Cell key={i} state={state}/>)}</div>
        <span className="rule-example-description">{description}</span>
    </div>;
}
export default function RulePage() {
    return <div className="rule-page">
        <div className="rules-container">
            <section><h1>Rules</h1></section>
            <section className="rule-objective">
                <h2>Reveal the hidden picture</h2>
                <p>Use the numbers to decide which cells to fill.<br/>Every row and column must match its clues.</p>
            </section>
            <section className="rule-states">
                <h2>Three cell states</h2>
                <div className="rule-state-legend">
                    <span><Cell state="filled"/>Filled</span>
                    <span><Cell state="empty"/>Empty</span>
                    <span><Cell state="unknown"/>Undecided</span>
                </div>
            </section>
            <section className="rule-clues">
                <h2>Read the numbers in order</h2>
                <Example clue="3" states={['filled','filled','filled','empty','empty']} description="One group of 3 filled cells."/>
                <Example clue="2 1" states={['filled','filled','empty','filled','empty']} description="A group of 2, then a group of 1."/>
                <p className="rule-gap-note">Leave at least one empty cell between groups.</p>
            </section>
        </div>
    </div>;
}
