import { readInput } from '../../shared/inputReader';

export function day03puzzle1(): string {
	const lines = readInput('03', false);
	let result = 0;
	for (let y = 0; y < lines.length; y++) {
		const line = lines[y];
		const numberState = {
			isAdjacentToSymbol: false,
			numStr: '',
		};
		for (let x = 0; x < line.length; x++) {
			const char = line.charAt(x);
			// possibly end of number
			if (char.match(/\d/) === null) {
				if (numberState.isAdjacentToSymbol) {
					result += parseInt(numberState.numStr);
					numberState.isAdjacentToSymbol = false;
				}
				numberState.numStr = '';
				continue;
			}
			numberState.numStr += char;
			// check if adjacent to a symbol (skip if already adjacent)
			if (!numberState.isAdjacentToSymbol) {
				const notSymbolRegex = /\d|\./;
				const isAdjacentToSymbol =
					(x > 0 && line.charAt(x - 1).match(notSymbolRegex) === null) ||
					(x < line.length - 1 && line.charAt(x + 1).match(notSymbolRegex) === null) ||
					(y > 0 && lines[y - 1].charAt(x).match(notSymbolRegex) === null) ||
					(y < lines.length - 1 && lines[y + 1].charAt(x).match(notSymbolRegex) === null) ||
					(x > 0 && y > 0 && lines[y - 1].charAt(x - 1).match(notSymbolRegex) === null) ||
					(x > 0 &&
						y < lines.length - 1 &&
						lines[y + 1].charAt(x - 1).match(notSymbolRegex) === null) ||
					(x < line.length - 1 &&
						y > 0 &&
						lines[y - 1].charAt(x + 1).match(notSymbolRegex) === null) ||
					(x < line.length - 1 &&
						y < lines.length - 1 &&
						lines[y + 1].charAt(x + 1).match(notSymbolRegex) === null);
				if (isAdjacentToSymbol) {
					numberState.isAdjacentToSymbol = true;
				}
			}
		}
		// check necessary for numbers that are at the end of a line
		if (numberState.isAdjacentToSymbol) {
			result += parseInt(numberState.numStr);
		}
	}
	return `${result}`;
}

interface NumberData {
	value: number;
	startIdx: number;
	endIdx: number;
}

function discoverNumber(line: string, digitIdx: number): NumberData {
	let resultStr = line.charAt(digitIdx);
	let cursor = digitIdx - 1;
	while (cursor >= 0) {
		const char = line.charAt(cursor);
		if (char.match(/\d/) === null) {
			break;
		}
		resultStr = char + resultStr;
		cursor--;
	}
	const startIdx = cursor + 1;
	cursor = digitIdx + 1;
	while (cursor < line.length) {
		const char = line.charAt(cursor);
		if (char.match(/\d/) === null) {
			break;
		}
		resultStr += char;
		cursor++;
	}
	const endIdx = cursor - 1;
	return {
		startIdx: startIdx,
		endIdx: endIdx,
		value: parseInt(resultStr),
	};
}

export function day03puzzle2(): string {
	const lines = readInput('03', false);
	let result = 0;
	for (let y = 0; y < lines.length; y++) {
		const line = lines[y];
		for (let x = 0; x < line.length; x++) {
			const char = line.charAt(x);
			if (char !== '*') {
				continue;
			}
			// find numbers around
			let numbersAround: NumberData[] = [];
			// above
			if (y > 0) {
				if (x > 0 && lines[y - 1].charAt(x - 1).match(/\d/) !== null) {
					numbersAround.push(discoverNumber(lines[y - 1], x - 1));
				}
				// else makes sense as there can't be separate numbers in these positions
				else if (lines[y - 1].charAt(x).match(/\d/) !== null) {
					numbersAround.push(discoverNumber(lines[y - 1], x));
				}
				if (x < line.length - 1 && lines[y - 1].charAt(x + 1).match(/\d/) !== null) {
					const newNumber = discoverNumber(lines[y - 1], x + 1);
					if (numbersAround.length === 0 || newNumber.startIdx !== numbersAround.at(-1)!.startIdx) {
						numbersAround.push(newNumber);
					}
				}
			}
			// to the left
			if (x > 0 && line.charAt(x - 1).match(/\d/) !== null) {
				numbersAround.push(discoverNumber(line, x - 1));
			}
			// to the right
			if (x < line.length - 1 && line.charAt(x + 1).match(/\d/) !== null) {
				numbersAround.push(discoverNumber(line, x + 1));
			}
			// below
			const numbersBelow: NumberData[] = [];
			if (y < lines.length - 1) {
				if (x > 0 && lines[y + 1].charAt(x - 1).match(/\d/) !== null) {
					numbersBelow.push(discoverNumber(lines[y + 1], x - 1));
				}
				// else makes sense as there can't be separate numbers in these positions
				else if (lines[y + 1].charAt(x).match(/\d/) !== null) {
					numbersBelow.push(discoverNumber(lines[y + 1], x));
				}
				if (x < line.length - 1 && lines[y + 1].charAt(x + 1).match(/\d/) !== null) {
					const newNumber = discoverNumber(lines[y + 1], x + 1);
					if (numbersBelow.length === 0 || newNumber.startIdx !== numbersBelow.at(-1)?.startIdx) {
						numbersBelow.push(newNumber);
					}
				}
			}
			numbersAround = [...numbersAround, ...numbersBelow];
			// what we're looking for
			if (numbersAround.length === 2) {
				const gearRatio = numbersAround.at(0)!.value * numbersAround.at(1)!.value;
				result += gearRatio;
			}
		}
	}
	return `${result}`;
}
