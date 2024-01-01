import { readInput } from '../../shared/inputReader';

interface ScratchCard {
	winningNumbers: Set<number>;
	myNumbers: number[];
}

function parseCards(lines: string[]): ScratchCard[] {
	const result: ScratchCard[] = [];
	for (let line of lines) {
		const [, winningNumbersStr, myNumbersStr] = line.split(/:|\|/);
		const winningNumbers = new Set(
			winningNumbersStr
				.trim()
				.split(/\s+/)
				.map((s) => parseInt(s))
		);
		const myNumbers = myNumbersStr
			.trim()
			.split(/\s+/)
			.map((s) => parseInt(s));
		result.push({ winningNumbers, myNumbers });
	}
	return result;
}

export function day04puzzle1(): string {
	const lines = readInput('04', false);
	const cards = parseCards(lines);
	let result = 0;
	for (let card of cards) {
		let matchingNumsCount = 0;
		for (let num of card.myNumbers) {
			if (card.winningNumbers.has(num)) {
				matchingNumsCount++;
			}
		}
		if (matchingNumsCount > 0) {
			const cardValue = 2 ** (matchingNumsCount - 1);
			result += cardValue;
		}
	}
	return `${result}`;
}

export function day04puzzle2(): string {
	const lines = readInput('04', false);
	const cards = parseCards(lines);
	const cardCounts = new Array(cards.length).fill(1);
	for (let i = 0; i < cards.length; i++) {
		const card = cards[i];
		let matchingNumsCount = 0;
		for (let num of card.myNumbers) {
			if (card.winningNumbers.has(num)) {
				matchingNumsCount++;
			}
		}
		for (let j = i + 1; j <= i + matchingNumsCount && j < cards.length; j++) {
			cardCounts[j] += cardCounts[i];
		}
	}
	const result = cardCounts.reduce((currSum, newEl) => currSum + newEl);
	return `${result}`;
}
