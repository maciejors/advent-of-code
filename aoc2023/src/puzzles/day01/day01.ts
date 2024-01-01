import { readInput } from '../../shared/inputReader';

export function day01puzzle1(): string {
	const lines = readInput('01', false);
	let sum = 0;
	for (let line of lines) {
		const matches = Array.from(line.matchAll(/\d/g));
		sum += parseInt(matches[0][0] + matches[matches.length - 1][0]);
	}
	return `${sum}`;
}

function reverseString(s: string): string {
	return s.split('').reverse().join('');
}

export function day01puzzle2(): string {
	const lines = readInput('01', false);
	// digitsText-digits maps
	let digitsTextMap = new Map<string, string>([
		['one', '1'],
		['two', '2'],
		['three', '3'],
		['four', '4'],
		['five', '5'],
		['six', '6'],
		['seven', '7'],
		['eight', '8'],
		['nine', '9'],
	]);
	let digitsTextMapReversed = new Map<string, string>();
	digitsTextMap.forEach((value, key) => {
		digitsTextMapReversed.set(reverseString(key), value);
	});
	let sum = 0;
	// one|two|...|nine|\d
	let regex = new RegExp([...digitsTextMap.keys(), '\\d'].join('|'));
	// eno|owt|...|enin|\d
	let regexReversed = new RegExp([...digitsTextMapReversed.keys(), '\\d'].join('|'));
	for (let line of lines) {
		// extract the first and the last match
		const firstMatch = line.match(regex)![0];
		const lastMatch = reverseString(line).match(regexReversed)![0];
		// convert spelled out numbers to digits
		const firstDigit = digitsTextMap.get(firstMatch) ?? firstMatch;
		const lastDigit = digitsTextMapReversed.get(lastMatch) ?? lastMatch;
		// concatenate them to get a final number
		const newNumber = parseInt(firstDigit + lastDigit);
		// add it to the total sum
		sum += newNumber;
	}
	return `${sum}`;
}
