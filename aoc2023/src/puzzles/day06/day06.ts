import { readInput } from '../../shared/inputReader';

interface RaceData {
	time: number;
	record: number;
}

function parseInput1(lines: string[]): RaceData[] {
	const timesStr = lines[0].split(/\s+/).slice(1);
	const recordsStr = lines[1].split(/\s+/).slice(1);
	const result: RaceData[] = [];
	for (let i = 0; i < timesStr.length; i++) {
		result.push({ time: parseInt(timesStr[i]), record: parseInt(recordsStr[i]) });
	}
	return result;
}

function parseInput2(lines: string[]): RaceData {
	const timeStr = lines[0].split(/\s+/).slice(1).join('');
	const recordStr = lines[1].split(/\s+/).slice(1).join('');
	return { time: parseInt(timeStr), record: parseInt(recordStr) };
}

export function day06puzzle1(): string {
	const lines = readInput('06', false);
	const races = parseInput1(lines);
	let result = 1;
	for (let race of races) {
		let winningStrategiesCount = 0;
		for (let buttonHeldTime = 1; buttonHeldTime < race.time; buttonHeldTime++) {
			const distanceTravelled = buttonHeldTime * (race.time - buttonHeldTime);
			if (distanceTravelled > race.record) {
				winningStrategiesCount++;
			}
		}
		result *= winningStrategiesCount;
	}
	return `${result}`;
}

export function day06puzzle2(): string {
	const lines = readInput('06', false);
	const race = parseInput2(lines);
	let winningStrategiesCount = 0;
	for (let buttonHeldTime = 1; buttonHeldTime < race.time; buttonHeldTime++) {
		const distanceTravelled = buttonHeldTime * (race.time - buttonHeldTime);
		if (distanceTravelled > race.record) {
			winningStrategiesCount++;
		}
	}
	return `${winningStrategiesCount}`;
}
