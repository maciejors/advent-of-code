import { readInput } from '../../shared/inputReader';

class Mapping {
	destRangeStarts: number[] = [];
	srcRangeStarts: number[] = [];
	rangeLengths: number[] = [];

	add(destinationRangeStart: number, sourceRangeStart: number, rangeLength: number): void {
		this.destRangeStarts.push(destinationRangeStart);
		this.srcRangeStarts.push(sourceRangeStart);
		this.rangeLengths.push(rangeLength);
	}

	map(source: number): number {
		for (let i = 0; i < this.rangeLengths.length; i++) {
			const destRangeStart = this.destRangeStarts[i];
			const srcRangeStart = this.srcRangeStarts[i];
			const rangeLength = this.rangeLengths[i];
			if (source >= srcRangeStart && source < srcRangeStart + rangeLength) {
				const offset = source - srcRangeStart;
				return destRangeStart + offset;
			}
		}
		// no mapping found
		return source;
	}
}

interface PuzzleInput {
	seeds: number[];
	mappings: Mapping[];
}

function parseInput(lines: string[]): PuzzleInput {
	// process seeds separately
	const seeds: number[] = lines[0]
		.split(' ')
		.slice(1) // get rid of the label
		.map((v) => parseInt(v));
	const mappings: Mapping[] = [];
	for (let line of lines.slice(1)) {
		// skip empty lines
		if (line.length === 0) {
			continue;
		}
		// lines with text begin new mapping
		if (line[0].match(/[a-z]/) !== null) {
			mappings.push(new Mapping());
			continue;
		}
		// lines with numbers are processed
		const [destinationRangeStart, sourceRangeStart, rangeLength] = line
			.split(' ')
			.map((v) => parseInt(v));
		mappings[mappings.length - 1].add(destinationRangeStart, sourceRangeStart, rangeLength);
	}
	return { seeds, mappings };
}

export function day05puzzle1(): string {
	const lines = readInput('05', false);
	const input = parseInput(lines);
	const locations: number[] = [];
	for (let seed of input.seeds) {
		let currValue = seed;
		for (let mapping of input.mappings) {
			currValue = mapping.map(currValue);
		}
		locations.push(currValue);
	}
	let result = Math.min(...locations);
	return `${result}`;
}

export function day05puzzle2(): string {
	const lines = readInput('05', false);
	const input = parseInput(lines);
	// Reparse the seeds (ineffective - leads to a memory error)
	const newSeeds: number[] = [];
	for (let i = 0; i < input.seeds.length; i += 2) {
		const seedStartRange = input.seeds[i];
		const seedRangeLength = input.seeds[i + 1];
		const actualSeeds = Array.from(new Array(seedRangeLength), (el, idx) => seedStartRange + idx);
		newSeeds.push(...actualSeeds);
	}
	input.seeds = newSeeds;
	const locations: number[] = [];
	for (let seed of input.seeds) {
		let currValue = seed;
		for (let mapping of input.mappings) {
			currValue = mapping.map(currValue);
		}
		locations.push(currValue);
	}
	let result = Math.min(...locations);
	return `${result}`;
}
