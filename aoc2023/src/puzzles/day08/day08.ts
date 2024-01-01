import { readInput } from '../../shared/inputReader';

interface NodeData {
	name: string;
	nextNodes: string[]; // two-element string
	isStartingNode: boolean;
	isEndingNode: boolean;
}

function parseNodes(nodesData: string[]): Map<string, NodeData> {
	const result: Map<string, NodeData> = new Map();
	for (let nodeData of nodesData) {
		const lineRegex = /(\w+) = \((\w+), (\w+)\)/g;
		const matches = [...nodeData.matchAll(lineRegex)];
		const nodeName = matches[0][1];
		const leftNodeName = matches[0][2];
		const rightNodeName = matches[0][3];
		result.set(nodeName, {
			name: nodeName,
			nextNodes: [leftNodeName, rightNodeName],
			isStartingNode: nodeName.endsWith('A'),
			isEndingNode: nodeName.endsWith('Z'),
		});
	}
	return result;
}

export function day08puzzle1(): string {
	const lines = readInput('08', false);
	let stepCount = 0;
	const directions = lines[0];
	const nodesMap = parseNodes(lines.slice(2));
	let currNode = 'AAA';
	while (currNode !== 'ZZZ') {
		const nextDirection = directions[stepCount % directions.length];
		switch (nextDirection) {
			case 'L':
				currNode = nodesMap.get(currNode)!.nextNodes[0];
				break;
			case 'R':
				currNode = nodesMap.get(currNode)!.nextNodes[1];
				break;
		}
		stepCount++;
	}
	return `${stepCount}`;
}

function getDivisors(num: number): number[] {
	const result: number[] = [];
	for (let div = 1; div <= Math.sqrt(num); div++) {
		if (num % div === 0) {
			result.push(div);
			result.push(num / div);
		}
	}
	// remove duplicate square root
	if (result[result.length - 1] === result[result.length - 2]) {
		result.pop();
	}
	result.sort((a, b) => a - b);
	return result;
}

/**
 * Greatest common divisor
 */
function gcd(a: number, b: number): number {
	const aDivisors = getDivisors(a);
	const bDivisors = getDivisors(b);
	const commonDivisors = aDivisors.filter((v) => bDivisors.includes(v));
	return commonDivisors[commonDivisors.length - 1];
}

/**
 * Least common multiple
 */
function lcm(a: number, b: number): number {
	return (a * b) / gcd(a, b);
}

export function day08puzzle2(): string {
	const lines = readInput('08', false);
	const directions = lines[0];
	const nodesMap = parseNodes(lines.slice(2));
	const startingNodes: NodeData[] = [];
	for (let nodeData of nodesMap.values()) {
		if (nodeData.isStartingNode) {
			startingNodes.push(nodeData);
		}
	}
	// how far from a starting node is an ending node
	const cycleLengths: number[] = [];
	// fill above arrays
	for (let startingNode of startingNodes) {
		let currNode = startingNode;
		let stepCountLocal = 0;
		while (!currNode.isEndingNode) {
			const nextDirection = directions[stepCountLocal % directions.length] == 'L' ? 0 : 1;
			currNode = nodesMap.get(currNode.nextNodes[nextDirection])!;
			stepCountLocal++;
		}
		cycleLengths.push(stepCountLocal);
	}
	// find least common multiple
	let result = cycleLengths.reduce((currLcm, newValue) => lcm(currLcm, newValue), cycleLengths[0]);
	return `${result}`;
}
