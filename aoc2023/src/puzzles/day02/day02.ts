import { readInput } from '../../shared/inputReader';

/**
 * @param lines Lines from the input file
 * @returns a map like this: {1: [{'green': 1, 'blue': 2}, {'red': 3, 'green': 3}, ...], 2: ...}
 */
function getGamesDataFromInput(lines: string[]): Map<number, Map<string, number>[]> {
	const result: Map<number, Map<string, number>[]> = new Map();
	for (let line of lines) {
		const gameIdRegex = /Game (\d+):/;
		const gameId = parseInt(line.match(gameIdRegex)![1]);
		const subsetsRaw = line.split(':', 2)[1].split(';');
		const gameMaps: Map<string, number>[] = [];
		for (let subsetRaw of subsetsRaw) {
			const colourRegex = /(\d+) ([a-zA-Z]+)/g;
			const subsetMap: Map<string, number> = new Map();
			for (let match of subsetRaw.matchAll(colourRegex)) {
				subsetMap.set(match[2], parseInt(match[1]));
			}
			gameMaps.push(subsetMap);
		}
		result.set(gameId, gameMaps);
	}
	return result;
}

export function day02puzzle1(): string {
	const lines = readInput('02', false);
	let result = 0;
	// games = {1: [{'green': 1, 'blue': 2}, {'red': 3, 'green': 3}, ...], 2: ...}
	const games = getGamesDataFromInput(lines);
	const maxRed = 12;
	const maxGreen = 13;
	const maxBlue = 14;
	games.forEach((gameMaps, gameId) => {
		for (let subsetMap of gameMaps) {
			const redCount = subsetMap.get('red') ?? 0;
			const greenCount = subsetMap.get('green') ?? 0;
			const blueCount = subsetMap.get('blue') ?? 0;
			if (redCount > maxRed || greenCount > maxGreen || blueCount > maxBlue) {
				return;
			}
		}
		result += gameId;
	});
	return `${result}`;
}

export function day02puzzle2(): string {
	const lines = readInput('02', false);
	let result = 0;
	// games = {1: [{'green': 1, 'blue': 2}, {'red': 3, 'green': 3}, ...], 2: ...}
	const games = getGamesDataFromInput(lines);
	games.forEach((gameMaps, gameId) => {
		let maxRed = 0;
		let maxGreen = 0;
		let maxBlue = 0;
		for (let subsetMap of gameMaps) {
			const redCount = subsetMap.get('red') ?? 0;
			const greenCount = subsetMap.get('green') ?? 0;
			const blueCount = subsetMap.get('blue') ?? 0;
			maxRed = redCount > maxRed ? redCount : maxRed;
			maxGreen = greenCount > maxGreen ? greenCount : maxGreen;
			maxBlue = blueCount > maxBlue ? blueCount : maxBlue;
		}
		const power = maxRed * maxGreen * maxBlue;
		result += power;
	});
	return `${result}`;
}
