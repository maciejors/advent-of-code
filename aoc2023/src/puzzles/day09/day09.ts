import { readInput } from '../../shared/inputReader';

function parseInput(lines: string[]): number[][] {
	const result: number[][] = [];
	for (let line of lines) {
		const numbers = line.split(/\s+/g).map((v) => parseInt(v));
		result.push(numbers);
	}
	return result;
}

export function day09puzzle1(): string {
	const lines = readInput('09', false);
	const histories = parseInput(lines);
	let result = 0;
	for (let history of histories) {
		const historyCopy = [...history];
		const sequences = [historyCopy];
		let lastSequence = sequences[sequences.length - 1];
		// get sequences
		while (!lastSequence.every((v) => v === 0)) {
			const newSequence: number[] = [];
			for (let i = 0; i < lastSequence.length - 1; i++) {
				newSequence.push(lastSequence[i + 1] - lastSequence[i]);
			}
			lastSequence = newSequence;
			sequences.push(lastSequence);
		}
		// calculate the prediction
		let prediction = 0;
		sequences.forEach((seq) => (prediction += seq[seq.length - 1]));
		result += prediction;
	}
	return `${result}`;
}

export function day09puzzle2(): string {
	const lines = readInput('09', false);
	const histories = parseInput(lines);
	let result = 0;
	for (let history of histories) {
		const historyCopy = [...history];
		const sequences = [historyCopy];
		let lastSequence = sequences[sequences.length - 1];
		// get sequences
		while (!lastSequence.every((v) => v === 0)) {
			const newSequence: number[] = [];
			for (let i = 0; i < lastSequence.length - 1; i++) {
				newSequence.push(lastSequence[i + 1] - lastSequence[i]);
			}
			lastSequence = newSequence;
			sequences.push(lastSequence);
		}
		// calculate the prediction
		let prediction = 0;
		sequences.reverse().forEach((seq) => {
			prediction = seq[0] - prediction;
		});
		result += prediction;
	}
	return `${result}`;
}
