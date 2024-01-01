import { readInput } from '../../shared/inputReader';

function getCardType(cards: number[]): number {
	const cardCounts: Map<number, number> = new Map();
	cards.forEach((cardValue) => {
		const currCount = cardCounts.get(cardValue) ?? 0;
		cardCounts.set(cardValue, currCount + 1);
	});
	// joker will add counts to the best card of those that have the highest count
	if (cards.includes(-1)) {
		let bestCardWithMaxCount = 0;
		let maxCount = 0;
		for (let [card, count] of cardCounts.entries()) {
			if (count > maxCount && card !== -1) {
				maxCount = count;
				bestCardWithMaxCount = card;
			} else if (count === maxCount && card > bestCardWithMaxCount) {
				bestCardWithMaxCount = card;
			}
		}
		cardCounts.set(bestCardWithMaxCount, maxCount + cardCounts.get(-1)!);
		cardCounts.delete(-1);
	}
	const countsArray = Array.from(cardCounts.values());
	// 7. five of a kind (5)
	if (countsArray.length === 1) {
		return 7;
	}
	// 6. four of a kind (4/1)
	if (countsArray.includes(4)) {
		return 6;
	}
	// 5. full house (3/2)
	if (countsArray.includes(3) && countsArray.includes(2)) {
		return 5;
	}
	// 4. three of a kind (3/1/1)
	if (countsArray.includes(3)) {
		return 4;
	}
	// 3. two pairs (2/2/1)
	if (
		countsArray.reduce((pairCount, newEl) => (newEl === 2 ? pairCount + 1 : pairCount), 0) === 2
	) {
		return 3;
	}
	// 2. one pair (2/1/1/1)
	if (countsArray.includes(2)) {
		return 2;
	}
	// 1. High card (1/1/1/1/1)
	return 1;
}

class Hand {
	cards: number[];
	bid: number;
	type: number;

	constructor(cards: number[], bid: number) {
		this.cards = cards;
		this.bid = bid;
		this.type = getCardType(cards);
	}

	compareTo(other: Hand): number {
		if (this.type !== other.type) {
			return this.type > other.type ? 1 : -1;
		}
		for (let i = 0; i < this.cards.length; i++) {
			if (this.cards[i] !== other.cards[i]) {
				return this.cards[i] > other.cards[i] ? 1 : -1;
			}
		}
		return 0;
	}
}

function parseInput(lines: string[], puzzleNo: number): Hand[] {
	const result: Hand[] = [];
	for (let line of lines) {
		const [handStr, bidStr] = line.split(' ');
		const bid = parseInt(bidStr);
		const cards = handStr.split('').map((cardText) => {
			let cardNum = parseInt(cardText);
			if (Number.isNaN(cardNum)) {
				switch (cardText) {
					case 'T':
						cardNum = 10;
						break;
					case 'J':
						cardNum = puzzleNo === 1 ? 11 : -1;
						break;
					case 'Q':
						cardNum = 12;
						break;
					case 'K':
						cardNum = 13;
						break;
					case 'A':
						cardNum = 14;
						break;
				}
			}
			return cardNum;
		});
		result.push(new Hand(cards, bid));
	}
	return result;
}

export function day07puzzle1(): string {
	const lines = readInput('07', false);
	const hands = parseInput(lines, 1);
	hands.sort((a, b) => a.compareTo(b));
	let result = 0;
	for (let i = 0; i < hands.length; i++) {
		result += (i + 1) * hands[i].bid;
	}
	return `${result}`;
}

export function day07puzzle2(): string {
	const lines = readInput('07', false);
	const hands = parseInput(lines, 2);
	hands.sort((a, b) => a.compareTo(b));
	let result = 0;
	for (let i = 0; i < hands.length; i++) {
		result += (i + 1) * hands[i].bid;
	}
	return `${result}`;
}
